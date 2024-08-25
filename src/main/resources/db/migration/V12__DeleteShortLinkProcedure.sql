ALTER TABLE Quily.users MODIFY COLUMN password TEXT;

# delete short link using short link url and email
USE `Quily`;
DROP procedure IF EXISTS `delete_short_link`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE delete_short_link(
 IN user_email_value VARCHAR(255),
 IN  shorted_link_value VARCHAR(255))
 BEGIN
 DELETE FROM short_link
 WHERE user_email = user_email_value AND shorted_link = shorted_link_value;
 END$$

 DELIMITER;
