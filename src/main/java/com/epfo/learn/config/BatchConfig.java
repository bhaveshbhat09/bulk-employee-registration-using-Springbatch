package com.epfo.learn.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private static final Logger log = LogManager.getLogger(BatchConfig.class);

    @Bean
    public Step helloStep(StepBuilderFactory steps) {
        return steps.get("helloStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Hello from the Spring Batch step");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Job helloJob(JobBuilderFactory jobs, Step helloStep) {
        return jobs.get("helloJob")
                .start(helloStep)
                .build();
    }

    @Bean
    public BatchConfigurer batchConfigurer(DataSource dataSource) {
        return new OracleBatchConfigurer(dataSource);
    }
}