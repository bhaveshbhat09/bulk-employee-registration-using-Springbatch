package com.epfo.learn.web;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReadTestController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job readJob;

    @GetMapping("/runread")
    public String runRead(@RequestParam(defaultValue = "E:/project/epfo-learn/data/employees.txt") String file)
            throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addString("filePath", file)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        JobExecution exec = jobLauncher.run(readJob, params);
        StringBuilder sb = new StringBuilder();
        sb.append("Job status: ").append(exec.getStatus()).append(", exit code: ")
          .append(exec.getExitStatus().getExitCode()).append("\n");
        for (StepExecution se : exec.getStepExecutions()) {
            sb.append("Step ").append(se.getStepName()).append(": read=").append(se.getReadCount())
              .append(", written=").append(se.getWriteCount()).append("\n");
        }
        for (Throwable t : exec.getAllFailureExceptions()) {
            sb.append("FAILURE: ").append(t).append("\n");
        }
        return sb.toString();
    }
}