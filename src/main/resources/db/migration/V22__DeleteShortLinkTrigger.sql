USE `Quily`;
DROP procedure IF EXISTS `short_link_update_trigger`;

DELIMITER $$
USE `Quily`$$
CREATE TRIGGER quily.short_link_delete_trigger
AFTER DELETE ON quily.short_link
FOR EACH ROW
BEGIN
    CALL quily.audit_log(OLD.id, 'original_link', OLD.original_link, NULL, 'deleted', OLD.user_email);

    CALL quily.audit_log(OLD.id, 'shorted_link', OLD.shorted_link, NULL, 'deleted', OLD.user_email);

    CALL quily.audit_log(OLD.id, 'expiry_date', OLD.expiry_date, NULL, 'deleted', OLD.user_email);
END; $$

DELIMITER ;