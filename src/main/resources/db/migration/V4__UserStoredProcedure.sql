# Create User

USE `Quily`;
DROP procedure IF EXISTS `create_user`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE create_user(
    IN email_value VARCHAR(255),
    IN user_name_value VARCHAR(255),
    IN password_value VARCHAR(255),
    IN is_active_value BOOLEAN
)
BEGIN
    INSERT INTO users (email, user_name, password, is_active)
    VALUES (email_value, user_name_value, password_value, is_active_value);
END$$

DELIMITER ;

# Make user active

USE `Quily`;
DROP procedure IF EXISTS `make_user_active`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE make_user_active(
    IN email_value VARCHAR(255)
)
BEGIN
    UPDATE users
    SET is_active = TRUE
    WHERE email = email_value;
END$$

DELIMITER ;

# Get User by email

USE `Quily`;
DROP procedure IF EXISTS `get_user_by_email`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE `get_user_by_email` (IN email_value VARCHAR(255))
BEGIN
    SELECT *
    FROM Quily.users
    WHERE email = email_value;
END$$

DELIMITER ;
