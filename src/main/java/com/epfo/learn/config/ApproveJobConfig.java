package com.epfo.learn.config;

import com.epfo.learn.batch.ApprovalDecision;
import com.epfo.learn.batch.ApprovalProcessor;
import com.epfo.learn.batch.ApprovalWriter;
import com.epfo.learn.batch.StagingRecord;
import com.epfo.learn.service.AadhaarVerifier;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ApproveJobConfig {

    @Bean
    @StepScope
    public JdbcCursorItemReader<StagingRecord> stagingCursorReader(
            DataSource dataSource,
            @Value("#{jobParameters['batchId']}") Long batchId) {
        return new JdbcCursorItemReaderBuilder<StagingRecord>()
                .name("stagingCursorReader")
                .dataSource(dataSource)
                .sql("SELECT staging_id, full_name, mobile, aadhaar, pan, passport, dob "
                        + "FROM employee_staging WHERE batch_id = ? AND status = 'PENDING' "
                        + "ORDER BY staging_id")
                .preparedStatementSetter(ps -> ps.setLong(1, batchId))
                .rowMapper((rs, i) -> new StagingRecord(
                        rs.getLong("staging_id"),
                        rs.getString("full_name"),
                        rs.getString("mobile"),
                        rs.getString("aadhaar"),
                        rs.getString("pan"),
                        rs.getString("passport"),
                        rs.getDate("dob")))
                .build();
    }

    @Bean
    @StepScope
    public ApprovalProcessor approvalProcessor(JdbcTemplate jdbcTemplate, AadhaarVerifier verifier) {
        return new ApprovalProcessor(jdbcTemplate, verifier);
    }

    @Bean
    @StepScope
    public ApprovalWriter approvalWriter(JdbcTemplate jdbcTemplate,
                                         @Value("#{jobParameters['batchId']}") Long batchId) {
        return new ApprovalWriter(jdbcTemplate, batchId);
    }

    @Bean
    public Step approveStep(StepBuilderFactory steps,
                            JdbcCursorItemReader<StagingRecord> stagingCursorReader,
                            ApprovalProcessor approvalProcessor,
                            ApprovalWriter approvalWriter) {
        return steps.get("approveStep")
                .<StagingRecord, ApprovalDecision>chunk(10)
                .reader(stagingCursorReader)
                .processor(approvalProcessor)
                .writer(approvalWriter)
                .build();
    }

    @Bean
    public Job approveJob(JobBuilderFactory jobs, Step approveStep) {
        return jobs.get("approveJob").start(approveStep).build();
    }
}