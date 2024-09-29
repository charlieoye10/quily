package com.example.quily.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Table(name = "audit_log")
public class AuditLog {
    @Id
    @NonNull
    private Integer audit_log_id;
    private String record_type;
    private String previous_value;
    private String current_value;
    private String action_type;
    private LocalDateTime performed_date;
    private String performed_by;
}
