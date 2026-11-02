package com.epfo.learn.web;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchTestController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job helloJob;

    @GetMapping("/runhello")
    public String runHello() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution execution = jobLauncher.run(helloJob, params);
        return "Job status: " + execution.getStatus()
                + ", exit code: " + execution.getExitStatus().getExitCode();
    }
}