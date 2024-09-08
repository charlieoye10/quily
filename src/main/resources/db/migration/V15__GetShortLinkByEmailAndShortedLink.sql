# Get short link by short url
USE `Quily`;
DROP procedure IF EXISTS `get_short_link_by_email_and_shorted_link`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE `get_short_link_by_email_and_shorted_link`(
 IN user_email_value VARCHAR(255),
 IN shorted_link_value TEXT)
 BEGIN
SELECT *  FROM short_link
 WHERE user_email = user_email_value AND shorted_link = shorted_link_value;
 END$$

DELIMITER ;


