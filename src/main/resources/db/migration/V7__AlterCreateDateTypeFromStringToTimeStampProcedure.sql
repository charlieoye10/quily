
ALTER TABLE email_confirmation_token
ADD COLUMN updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

ALTER TABLE email_confirmation_token
MODIFY COLUMN created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP;


USE `Quily`;
DROP PROCEDURE IF EXISTS `save_email_confirmation_token`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE save_email_confirmation_token(
    IN confirmation_token_value VARCHAR(255),
    IN user_email_value VARCHAR(255)
)
BEGIN
    INSERT INTO email_confirmation_token (confirmation_token, user_email)
    VALUES (confirmation_token_value, user_email_value)
    ON DUPLICATE KEY UPDATE
        updated_time = NOW(),
        confirmation_token = confirmation_token_value;
END$$

DELIMITER ;
