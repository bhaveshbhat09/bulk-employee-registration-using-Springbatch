package com.epfo.learn.config;

import com.epfo.learn.batch.EmployeeLineProcessor;
import com.epfo.learn.batch.LineResult;
import com.epfo.learn.batch.RawLine;
import com.epfo.learn.batch.StagingRow;
import com.epfo.learn.batch.StagingRowProcessor;
import com.epfo.learn.batch.ValidationOutcomeListener;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class ValidateJobConfig {

    // ---------- reading (used by both steps) ----------

    @Bean
    @StepScope
    public FlatFileItemReader<RawLine> rawLineReader(
            @Value("#{jobParameters['filePath']}") String filePath) {
        return new FlatFileItemReaderBuilder<RawLine>()
                .name("rawLineReader")
                .resource(new FileSystemResource(filePath))
                .encoding("UTF-8")
                .lineMapper((line, lineNumber) -> new RawLine(lineNumber, line))
                .build();
    }

    // ---------- step A: validate and write the error file ----------

    @Bean
    public ItemProcessor<RawLine, LineResult> employeeLineProcessor() {
        return new EmployeeLineProcessor();
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<LineResult> errorFileWriter(
            @Value("#{jobParameters['errorFilePath']}") String errorFilePath) {
        return new FlatFileItemWriterBuilder<LineResult>()
                .name("errorFileWriter")
                .resource(new FileSystemResource(errorFilePath))
                .encoding("UTF-8")
                .shouldDeleteIfEmpty(true)
                .headerCallback(w -> w.write("LINE | FIELD | REASON | ORIGINAL LINE"))
                .lineAggregator(result -> result.getErrors().stream()
                        .map(e -> e.getLineNumber() + " | " + e.getField() + " | "
                                + e.getReason() + " | " + result.getRawText())
                        .collect(Collectors.joining(System.lineSeparator())))
                .build();
    }

    @Bean
    public Step validateStep(StepBuilderFactory steps,
                             FlatFileItemReader<RawLine> rawLineReader,
                             ItemProcessor<RawLine, LineResult> employeeLineProcessor,
                             FlatFileItemWriter<LineResult> errorFileWriter) {
        return steps.get("validateStep")
                .<RawLine, LineResult>chunk(10)
                .reader(rawLineReader)
                .processor(employeeLineProcessor)
                .writer(errorFileWriter)
                .listener(new ValidationOutcomeListener())
                .build();
    }

    // ---------- step B: load staging ----------

    @Bean
    @StepScope
    public StagingRowProcessor stagingRowProcessor(
            @Value("#{jobParameters['batchId']}") Long batchId) {
        return new StagingRowProcessor(batchId);
    }

    @Bean
    public JdbcBatchItemWriter<StagingRow> stagingWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<StagingRow>()
                .dataSource(dataSource)
                .sql("INSERT INTO employee_staging "
                        + "(batch_id, line_no, full_name, mobile, aadhaar, pan, passport, dob) "
                        + "VALUES (:batchId, :lineNo, :fullName, :mobile, :aadhaar, :pan, :passport, :dob)")
                .beanMapped()
                .build();
    }

    @Bean
    public Step loadStagingStep(StepBuilderFactory steps,
                                FlatFileItemReader<RawLine> rawLineReader,
                                StagingRowProcessor stagingRowProcessor,
                                JdbcBatchItemWriter<StagingRow> stagingWriter) {
        return steps.get("loadStagingStep")
                .<RawLine, StagingRow>chunk(10)
                .reader(rawLineReader)
                .processor(stagingRowProcessor)
                .writer(stagingWriter)
                .build();
    }

    // ---------- the job: validate, then load only if the file was clean ----------

    @Bean
    public Job validateJob(JobBuilderFactory jobs, Step validateStep, Step loadStagingStep) {
        return jobs.get("validateJob")
                .start(validateStep).on("REJECTED").end()
                .from(validateStep).on("COMPLETED").to(loadStagingStep)
                .from(validateStep).on("FAILED").fail()
                .end()
                .build();
    }
}