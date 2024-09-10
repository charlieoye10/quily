USE `Quily`;
DROP procedure IF EXISTS `update_username`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE update_username(
    IN inputEmail VARCHAR(255),
    IN newUserName VARCHAR(255)
)
BEGIN
    UPDATE users
    SET user_name = newUserName
    WHERE email = inputEmail;
END$$

DELIMITER ;