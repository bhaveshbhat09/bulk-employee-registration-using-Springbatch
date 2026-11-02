package com.epfo.learn.batch;

public class RawLine {
    private final int lineNumber;
    private final String text;

    public RawLine(int lineNumber, String text) {
        this.lineNumber = lineNumber;
        this.text = text;
    }
    public int getLineNumber() { return lineNumber; }
    public String getText() { return text; }
}