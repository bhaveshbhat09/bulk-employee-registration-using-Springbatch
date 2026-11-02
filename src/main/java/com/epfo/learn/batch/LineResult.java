package com.epfo.learn.batch;

import java.util.List;

public class LineResult {
    private final int lineNumber;
    private final String rawText;
    private final List<LineError> errors;

    public LineResult(int lineNumber, String rawText, List<LineError> errors) {
        this.lineNumber = lineNumber;
        this.rawText = rawText;
        this.errors = errors;
    }
    public int getLineNumber() { return lineNumber; }
    public String getRawText() { return rawText; }
    public List<LineError> getErrors() { return errors; }
}