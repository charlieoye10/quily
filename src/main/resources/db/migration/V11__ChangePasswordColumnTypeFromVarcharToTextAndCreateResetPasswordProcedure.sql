ALTER TABLE Quily.users MODIFY COLUMN password TEXT;

# Reset password using email
USE `Quily`;
DROP procedure IF EXISTS `reset_password`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE reset_password(
IN email_value VARCHAR(255),
 IN password_value VARCHAR(255))
 BEGIN
 UPDATE users
    SET password = password_value
    WHERE email = email_value;
 END$$

 DELIMITER;
