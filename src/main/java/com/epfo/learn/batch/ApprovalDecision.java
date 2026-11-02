package com.epfo.learn.batch;

public class ApprovalDecision {
    private final StagingRecord record;
    private final boolean move;
    private final String reason;

    private ApprovalDecision(StagingRecord record, boolean move, String reason) {
        this.record = record;
        this.move = move;
        this.reason = reason;
    }

    public static ApprovalDecision move(StagingRecord record) {
        return new ApprovalDecision(record, true, null);
    }

    public static ApprovalDecision reject(StagingRecord record, String reason) {
        return new ApprovalDecision(record, false, reason);
    }

    public StagingRecord getRecord() { return record; }
    public boolean isMove() { return move; }
    public String getReason() { return reason; }
}