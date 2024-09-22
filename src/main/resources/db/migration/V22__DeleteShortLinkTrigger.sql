USE `Quily`;
DROP procedure IF EXISTS `short_link_update_trigger`;

DELIMITER $$
USE `Quily`$$
CREATE TRIGGER Quily.short_link_delete_trigger
AFTER DELETE ON Quily.short_link
FOR EACH ROW
BEGIN
    CALL Quily.audit_log(OLD.id, 'original_link', OLD.original_link, NULL, 'deleted', OLD.user_email);

    CALL Quily.audit_log(OLD.id, 'shorted_link', OLD.shorted_link, NULL, 'deleted', OLD.user_email);

    CALL Quily.audit_log(OLD.id, 'expiry_date', OLD.expiry_date, NULL, 'deleted', OLD.user_email);
END; $$

DELIMITER ;