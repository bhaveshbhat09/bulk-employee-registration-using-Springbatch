package com.epfo.learn.batch;

public class LineError {
    private final int lineNumber;
    private final String field;
    private final String reason;

    public LineError(int lineNumber, String field, String reason) {
        this.lineNumber = lineNumber;
        this.field = field;
        this.reason = reason;
    }
    public int getLineNumber() { return lineNumber; }
    public String getField() { return field; }
    public String getReason() { return reason; }

    @Override
    public String toString() {
        return "line " + lineNumber + " | " + field + " | " + reason;
    }
}