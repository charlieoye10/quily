USE `Quily`;
DROP procedure IF EXISTS `short_link_update_trigger`;

DELIMITER $$
USE `Quily`$$
CREATE TRIGGER Quily.short_link_update_trigger
AFTER UPDATE ON Quily.short_link
FOR EACH ROW
BEGIN
    IF NEW.original_link <> OLD.original_link THEN
        CALL Quily.audit_log(OLD.id, 'original_link', OLD.original_link, NEW.original_link, 'modified', OLD.user_email);
    END IF;

    IF NEW.shorted_link <> OLD.shorted_link THEN
        CALL Quily.audit_log(OLD.id, 'shorted_link', OLD.shorted_link, NEW.shorted_link, 'modified', OLD.user_email);
    END IF;

    IF NEW.expiry_date <> OLD.expiry_date THEN
        CALL Quily.audit_log(OLD.id, 'expiry_date', OLD.expiry_date, NEW.expiry_date, 'modified', OLD.user_email);
    END IF;
END; $$

DELIMITER ;