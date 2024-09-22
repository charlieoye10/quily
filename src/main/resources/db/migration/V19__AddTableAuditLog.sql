CREATE TABLE quily.audit_log (
    audit_log_id INT AUTO_INCREMENT PRIMARY KEY,
    record_id TEXT,
    record_type TEXT,
    previous_value TEXT,
    current_value TEXT,
    action_type ENUM('modified', 'deleted'),
    performed_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    performed_by VARCHAR(255)
);