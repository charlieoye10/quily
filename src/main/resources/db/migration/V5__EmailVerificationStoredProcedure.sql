#Save email verification token
USE `Quily`;
DROP procedure IF EXISTS `save_email_confirmation_token`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE save_email_confirmation_token(
    IN confirmation_token_value VARCHAR(255),
    IN created_time_value VARCHAR(255),
    IN user_email_value VARCHAR(255)
)
BEGIN
    INSERT INTO email_confirmation_token (confirmation_token, created_time, user_email)
    VALUES (confirmation_token_value, created_time_value, user_email_value)
    ON DUPLICATE KEY UPDATE
                         created_time = created_time_value;
END$$

DELIMITER ;

# Get Token
USE `Quily`;
DROP procedure IF EXISTS `get_email_confirmation_token`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE `get_email_confirmation_token` (IN token_value VARCHAR(255))
BEGIN
    SELECT *
    FROM email_confirmation_token
    WHERE confirmation_token = token_value;
END$$

DELIMITER ;

#Delete token
USE `Quily`;
DROP procedure IF EXISTS `delete_email_verification_token`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE `delete_email_verification_token` (IN token_value VARCHAR(255))
BEGIN
    DELETE FROM email_confirmation_token
    WHERE confirmation_token = token_value;
END$$

DELIMITER ;