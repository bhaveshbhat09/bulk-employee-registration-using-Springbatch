package com.epfo.learn.batch;

import java.util.List;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

public class ApprovalWriter implements ItemWriter<ApprovalDecision> {

    private final JdbcTemplate jdbc;
    private final long batchId;

    public ApprovalWriter(JdbcTemplate jdbc, long batchId) {
        this.jdbc = jdbc;
        this.batchId = batchId;
    }

    @Override
    public void write(List<? extends ApprovalDecision> items) {
        for (ApprovalDecision d : items) {
            StagingRecord r = d.getRecord();
            if (d.isMove()) {
                jdbc.update("INSERT INTO employee_master "
                                + "(full_name, mobile, aadhaar, pan, passport, dob, source_batch_id) "
                                + "VALUES (?, ?, ?, ?, ?, ?, ?)",
                        r.getFullName(), r.getMobile(), r.getAadhaar(), r.getPan(),
                        r.getPassport(), r.getDob(), batchId);
                jdbc.update("UPDATE employee_staging SET status = 'MOVED' WHERE staging_id = ?",
                        r.getStagingId());
            } else {
                jdbc.update("UPDATE employee_staging SET status = 'REJECTED', reject_reason = ? "
                                + "WHERE staging_id = ?",
                        d.getReason(), r.getStagingId());
            }
        }
    }
}