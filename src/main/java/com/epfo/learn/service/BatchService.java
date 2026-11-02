package com.epfo.learn.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.PreparedStatement;

@Service
public class BatchService {

    private static final Logger log = LogManager.getLogger(BatchService.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job validateJob;

    @Autowired
    private AppStorage storage;

    public BatchInfo upload(MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please choose a non-empty file");
        }
        long batchId = createBatch(cleanName(file.getOriginalFilename()));
        try {
            Path uploadPath = storage.uploadFile(batchId);
            Files.createDirectories(uploadPath.getParent());
            file.transferTo(uploadPath);
            runJob(batchId, uploadPath);
        } catch (Exception e) {
            log.error("Batch {} failed", batchId, e);
            jdbcTemplate.update("UPDATE upload_batch SET status = 'FAILED' WHERE batch_id = ?", batchId);
            throw e;
        }
        return getBatch(batchId);
    }

    public BatchInfo getBatch(long batchId) {
        List<BatchInfo> rows = jdbcTemplate.query(
                "SELECT batch_id, file_name, status, total_lines, error_lines, uploaded_at "
                        + "FROM upload_batch WHERE batch_id = ?",
                (rs, i) -> new BatchInfo(
                        rs.getLong("batch_id"),
                        rs.getString("file_name"),
                        rs.getString("status"),
                        rs.getLong("total_lines"),
                        rs.getLong("error_lines"),
                        rs.getTimestamp("uploaded_at").toString()),
                batchId);
        return rows.isEmpty() ? null : rows.get(0);
    }

    private void runJob(long batchId, Path uploadPath) throws Exception {
        Path errorPath = storage.errorFile(batchId);
        JobParameters params = new JobParametersBuilder()
                .addString("filePath", uploadPath.toString())
                .addString("errorFilePath", errorPath.toString())
                .addLong("batchId", batchId)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution exec = jobLauncher.run(validateJob, params);

        StepExecution validateExec = null;
        for (StepExecution se : exec.getStepExecutions()) {
            if ("validateStep".equals(se.getStepName())) validateExec = se;
        }
        long totalLines = validateExec == null ? 0 : validateExec.getReadCount();
        long errorLines = validateExec == null ? 0 : validateExec.getWriteCount();

        String status;
        if (exec.getStatus() != BatchStatus.COMPLETED) {
            status = "FAILED";
            log.error("Batch {} job failed: {}", batchId, exec.getAllFailureExceptions());
        } else if (validateExec != null && "REJECTED".equals(validateExec.getExitStatus().getExitCode())) {
            status = "REJECTED";
        } else {
            status = "STAGED";
        }

        // Oracle stores an empty string as NULL, so only rejected batches get an error file path.
        jdbcTemplate.update(
                "UPDATE upload_batch SET status = ?, total_lines = ?, error_lines = ?, error_file_path = ? "
                        + "WHERE batch_id = ?",
                status, totalLines, errorLines, "REJECTED".equals(status) ? errorPath.toString() : "", batchId);
    }

    private long createBatch(String fileName) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO upload_batch (file_name) VALUES (?)", new String[] { "BATCH_ID" });
            ps.setString(1, fileName);
            return ps;
        }, keys);
        return keys.getKey().longValue();
    }

    // Keep only the last path segment and cap the length. Used for display only, never as a path.
    private String cleanName(String original) {
        if (original == null || original.trim().isEmpty()) return "unnamed";
        String name = original.substring(Math.max(original.lastIndexOf('/'), original.lastIndexOf('\\')) + 1);
        return name.length() > 255 ? name.substring(0, 255) : name;
    }
}