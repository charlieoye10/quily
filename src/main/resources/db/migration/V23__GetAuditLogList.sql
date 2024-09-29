USE `Quily`;
DROP procedure IF EXISTS `get_audit_logs`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE get_audit_logs(
    IN page_number INT,
    IN page_size INT,
    IN p_record_type VARCHAR(255),
    IN p_performed_by VARCHAR(255)
)
BEGIN
    DECLARE offset INT;
    SET offset = (page_number - 1) * page_size;
    SELECT *
        FROM audit_log
        WHERE performed_by = p_performed_by
        	AND (p_record_type IS NULL OR record_type = p_record_type)
        ORDER BY audit_log_id DESC
    LIMIT page_size OFFSET offset;

END$$

DELIMITER ;