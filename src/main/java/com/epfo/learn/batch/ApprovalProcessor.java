package com.epfo.learn.batch;

import com.epfo.learn.service.AadhaarVerifier;
import java.util.HashSet;
import java.util.Set;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.JdbcTemplate;

public class ApprovalProcessor implements ItemProcessor<StagingRecord, ApprovalDecision> {

    private final JdbcTemplate jdbc;
    private final AadhaarVerifier verifier;
    private final Set<String> seenInThisRun = new HashSet<>();

    public ApprovalProcessor(JdbcTemplate jdbc, AadhaarVerifier verifier) {
        this.jdbc = jdbc;
        this.verifier = verifier;
    }

    @Override
    public ApprovalDecision process(StagingRecord r) {
        String reason = verifier.verify(r.getAadhaar());
        if (reason != null) {
            return ApprovalDecision.reject(r, reason);
        }

        Integer inMaster = jdbc.queryForObject(
                "SELECT COUNT(*) FROM employee_master WHERE aadhaar = ?", Integer.class, r.getAadhaar());
        if (inMaster != null && inMaster > 0) {
            return ApprovalDecision.reject(r, "Aadhaar is already registered");
        }

        if (!seenInThisRun.add(r.getAadhaar())) {
            return ApprovalDecision.reject(r, "Same Aadhaar appears earlier in this file");
        }
        return ApprovalDecision.move(r);
    }
}