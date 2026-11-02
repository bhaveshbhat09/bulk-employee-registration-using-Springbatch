CREATE TABLE upload_batch (
    batch_id        NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    file_name       VARCHAR2(255)  NOT NULL,
    status          VARCHAR2(20)   DEFAULT 'UPLOADED' NOT NULL,
    total_lines     NUMBER,
    error_lines     NUMBER,
    error_file_path VARCHAR2(500),
    uploaded_at     TIMESTAMP      DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT ck_batch_status CHECK (status IN ('UPLOADED','REJECTED','STAGED','APPROVED','COMPLETED','FAILED'))
);

CREATE TABLE employee_staging (
    staging_id     NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    batch_id       NUMBER        NOT NULL,
    line_no        NUMBER        NOT NULL,
    full_name      VARCHAR2(100) NOT NULL,
    mobile         VARCHAR2(10)  NOT NULL,
    aadhaar        VARCHAR2(12)  NOT NULL,
    pan            VARCHAR2(10)  NOT NULL,
    passport       VARCHAR2(8)   NOT NULL,
    dob            DATE          NOT NULL,
    status         VARCHAR2(20)  DEFAULT 'PENDING' NOT NULL,
    reject_reason  VARCHAR2(500),
    created_at     TIMESTAMP     DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT fk_staging_batch FOREIGN KEY (batch_id) REFERENCES upload_batch (batch_id),
    CONSTRAINT uq_staging_line UNIQUE (batch_id, line_no),
    CONSTRAINT ck_staging_status CHECK (status IN ('PENDING','VERIFIED','REJECTED','MOVED'))
);

CREATE INDEX ix_staging_batch ON employee_staging (batch_id);  