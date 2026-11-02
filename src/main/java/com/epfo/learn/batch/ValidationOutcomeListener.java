package com.epfo.learn.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class ValidationOutcomeListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        // nothing to do before the step
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        boolean finishedOk = ExitStatus.COMPLETED.getExitCode()
                .equals(stepExecution.getExitStatus().getExitCode());
        if (finishedOk && stepExecution.getWriteCount() > 0) {
            return new ExitStatus("REJECTED");
        }
        return stepExecution.getExitStatus();
    }
}