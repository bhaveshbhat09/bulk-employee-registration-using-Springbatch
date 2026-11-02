package com.epfo.learn.service;

public class BatchInfo {
    private final long batchId;
    private final String fileName;
    private final String status;
    private final long totalLines;
    private final long errorLines;
    private final String uploadedAt;

    public BatchInfo(long batchId, String fileName, String status,
                     long totalLines, long errorLines, String uploadedAt) {
        this.batchId = batchId;
        this.fileName = fileName;
        this.status = status;
        this.totalLines = totalLines;
        this.errorLines = errorLines;
        this.uploadedAt = uploadedAt;
    }

    public long getBatchId() { return batchId; }
    public String getFileName() { return fileName; }
    public String getStatus() { return status; }
    public long getTotalLines() { return totalLines; }
    public long getErrorLines() { return errorLines; }
    public String getUploadedAt() { return uploadedAt; }
    public boolean isErrorFileAvailable() { return "REJECTED".equals(status); }
}