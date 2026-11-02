package com.epfo.learn.batch;

import java.time.LocalDate;
import java.util.regex.Pattern;
import org.springframework.batch.item.ItemProcessor;

public class StagingRowProcessor implements ItemProcessor<RawLine, StagingRow> {

    private final long batchId;

    public StagingRowProcessor(long batchId) {
        this.batchId = batchId;
    }

    @Override
    public StagingRow process(RawLine raw) {
        // The file already passed validation in step A, so every line has 6 good fields.
        String[] f = raw.getText().split(Pattern.quote("#~#"), -1);
        return new StagingRow(batchId, raw.getLineNumber(),
                f[0].trim(), f[1].trim(), f[2].trim(), f[3].trim(), f[4].trim(),
                java.sql.Date.valueOf(LocalDate.parse(f[5].trim())));
    }
}