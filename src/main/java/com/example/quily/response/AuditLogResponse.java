package com.example.quily.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AuditLogResponse {
    private String record_id;
    private String record_type;
    private String previous_value;
    private String current_value;
    private String action_type;
    private LocalDateTime performed_date;
    private String performed_by;
}
