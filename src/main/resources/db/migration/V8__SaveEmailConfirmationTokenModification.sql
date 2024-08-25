USE `Quily`;
DROP procedure IF EXISTS `save_email_confirmation_token`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE save_email_confirmation_token(
    IN confirmation_token_value VARCHAR(255),
    IN user_email_value VARCHAR(255)
)
BEGIN
    DECLARE affected_rows INT;

    INSERT INTO email_confirmation_token (confirmation_token, user_email)
    VALUES (confirmation_token_value, user_email_value)
    ON DUPLICATE KEY UPDATE
                         updated_time = NOW(),
                         confirmation_token = confirmation_token_value;

    SET affected_rows = ROW_COUNT();
    SELECT affected_rows AS affectedRows;

END$$

DELIMITER ;
