CREATE TABLE employee_master (
    employee_id     NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    full_name       VARCHAR2(100) NOT NULL,
    mobile          VARCHAR2(10)  NOT NULL,
    aadhaar         VARCHAR2(12)  NOT NULL,
    pan             VARCHAR2(10)  NOT NULL,
    passport        VARCHAR2(8)   NOT NULL,
    dob             DATE          NOT NULL,
    source_batch_id NUMBER        NOT NULL,
    created_at      TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT uq_master_aadhaar UNIQUE (aadhaar),
    CONSTRAINT fk_master_batch FOREIGN KEY (source_batch_id) REFERENCES upload_batch (batch_id)
);

ALTER TABLE upload_batch ADD (
    approved_by   VARCHAR2(100),
    approved_at   TIMESTAMP,
    moved_rows    NUMBER,
    rejected_rows NUMBER
);