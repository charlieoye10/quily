USE `Quily`;
DROP procedure IF EXISTS `audit_log`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE quily.audit_log(
    IN p_record_id TEXT,
    IN p_record_type TEXT,
    IN p_previous_value TEXT,
    IN p_current_value TEXT,
    IN p_action_type ENUM('modified', 'deleted'),
    IN p_performed_by VARCHAR(255)
)
BEGIN
    INSERT INTO quily.audit_log (record_id, record_type, previous_value, current_value, action_type, performed_by)
    VALUES (p_record_id, p_record_type, p_previous_value, p_current_value, p_action_type, p_performed_by);
END; $$

DELIMITER ;