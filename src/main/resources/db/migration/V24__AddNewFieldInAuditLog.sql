ALTER TABLE Quily.audit_log
ADD COLUMN data_entity TEXT;

USE `Quily`;
DROP procedure IF EXISTS `audit_log`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE Quily.audit_log(
    IN p_record_id TEXT,
    IN p_record_type TEXT,
    IN p_previous_value TEXT,
    IN p_current_value TEXT,
    IN p_action_type ENUM('modified', 'deleted'),
    IN p_performed_by VARCHAR(255),
    IN p_data_entity VARCHAR(255)
)
BEGIN
    INSERT INTO Quily.audit_log (record_id, record_type, previous_value, current_value, action_type, performed_by,data_entity)
    VALUES (p_record_id, p_record_type, p_previous_value, p_current_value, p_action_type, p_performed_by,p_data_entity);
END; $$

DELIMITER ;

USE `Quily`;
DROP TRIGGER IF EXISTS `short_link_update_trigger`;

DELIMITER $$
USE `Quily`$$
CREATE TRIGGER Quily.short_link_update_trigger
AFTER UPDATE ON Quily.short_link
FOR EACH ROW
BEGIN
    IF NEW.original_link <> OLD.original_link THEN
        CALL Quily.audit_log(OLD.id, 'original_link', OLD.original_link, NEW.original_link, 'modified', OLD.user_email,'short_link');
    END IF;

    IF NEW.shorted_link <> OLD.shorted_link THEN
        CALL Quily.audit_log(OLD.id, 'shorted_link', OLD.shorted_link, NEW.shorted_link, 'modified', OLD.user_email,'short_link');
    END IF;

    IF NEW.expiry_date <> OLD.expiry_date THEN
        CALL Quily.audit_log(OLD.id, 'expiry_date', OLD.expiry_date, NEW.expiry_date, 'modified', OLD.user_email,'short_link');
    END IF;
END; $$

DELIMITER ;

USE `Quily`;
DROP TRIGGER IF EXISTS `short_link_delete_trigger`;

DELIMITER $$
USE `Quily`$$
CREATE TRIGGER Quily.short_link_delete_trigger
AFTER DELETE ON Quily.short_link
FOR EACH ROW
BEGIN
    CALL Quily.audit_log(OLD.id, 'original_link', OLD.original_link, NULL, 'deleted', OLD.user_email,'short_link');

    CALL Quily.audit_log(OLD.id, 'shorted_link', OLD.shorted_link, NULL, 'deleted', OLD.user_email,'short_link');

    CALL Quily.audit_log(OLD.id, 'expiry_date', OLD.expiry_date, NULL, 'deleted', OLD.user_email,'short_link');
END; $$

DELIMITER ;

USE `Quily`;
DROP procedure IF EXISTS `get_audit_logs`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE get_audit_logs(
    IN page_number INT,
    IN page_size INT,
    IN p_performed_by VARCHAR(255),
    IN p_data_entity VARCHAR(255)
)
BEGIN
    DECLARE offset INT;
    SET offset = (page_number - 1) * page_size;
    SELECT *
        FROM audit_log
        WHERE performed_by = p_performed_by
        	AND (p_data_entity IS NULL OR data_entity = p_data_entity)
        ORDER BY audit_log_id DESC
    LIMIT page_size OFFSET offset;

END$$

DELIMITER ;

USE `Quily`;
DROP TRIGGER IF EXISTS `user_update_trigger`;

DELIMITER $$
USE `Quily`$$
CREATE TRIGGER Quily.user_update_trigger
AFTER UPDATE ON Quily.users
FOR EACH ROW
BEGIN
    IF NEW.user_name <> OLD.user_name THEN
        CALL Quily.audit_log(OLD.email, 'user_name', OLD.user_name, NEW.user_name, 'modified', OLD.email,'users');
    END IF;

    IF NEW.password <> OLD.password THEN
        CALL Quily.audit_log(OLD.email, 'password', OLD.password, NEW.password, 'modified', OLD.email,'users');
    END IF;
END; $$

DELIMITER ;

