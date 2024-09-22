USE `Quily`;
DROP procedure IF EXISTS `get_short_link_by_url`;

DELIMITER $$
USE `Quily`$$
CREATE procedure get_short_link_by_url(IN url text)
BEGIN
    SELECT * FROM Quily.short_link where BINARY shorted_link = url;
END;$$

DELIMITER ;



USE `Quily`;
DROP procedure IF EXISTS `delete_short_links`;

create PROCEDURE `delete_short_links`(IN email_value VARCHAR(255), IN url_value TEXT)
BEGIN
    SET @formatted_url_value = REPLACE(url_value, ',', ''',''');
    SET @formatted_url_value = CONCAT('''', @formatted_url_value, '''');

    SET @sql = CONCAT('DELETE FROM short_link WHERE BINARY shorted_link IN (', @formatted_url_value, ') AND BINARY user_email = ', QUOTE(email_value));

    PREPARE stmt FROM @sql;
    EXECUTE stmt;

    DEALLOCATE PREPARE stmt;
END
