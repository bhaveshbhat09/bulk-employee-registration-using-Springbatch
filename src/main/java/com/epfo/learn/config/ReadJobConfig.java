package com.epfo.learn.config;

import com.epfo.learn.batch.EmployeeRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class ReadJobConfig {

    private static final Logger log = LogManager.getLogger(ReadJobConfig.class);

    @Bean
    @StepScope
    public FlatFileItemReader<EmployeeRecord> employeeReader(
            @Value("#{jobParameters['filePath']}") String filePath) {
        return new FlatFileItemReaderBuilder<EmployeeRecord>()
                .name("employeeReader")
                .resource(new FileSystemResource(filePath))
                .encoding("UTF-8")
                .delimited()
                .delimiter("#~#")
                .names("name", "mobile", "aadhaar", "pan", "passport", "dob")
                .targetType(EmployeeRecord.class)
                .build();
    }

    @Bean
    public ItemWriter<EmployeeRecord> logWriter() {
        return items -> {
            for (EmployeeRecord r : items) {
                log.debug("READ -> {}", r);
            }
        };
    }

    @Bean
    public Step readStep(StepBuilderFactory steps,
                         FlatFileItemReader<EmployeeRecord> employeeReader,
                         ItemWriter<EmployeeRecord> logWriter) {
        return steps.get("readStep")
                .<EmployeeRecord, EmployeeRecord>chunk(2)
                .reader(employeeReader)
                .writer(logWriter)
                .build();
    }

    @Bean
    public Job readJob(JobBuilderFactory jobs, Step readStep) {
        return jobs.get("readJob").start(readStep).build();
    }
}