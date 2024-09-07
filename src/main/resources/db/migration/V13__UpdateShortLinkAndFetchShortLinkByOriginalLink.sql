USE `Quily`;
DROP procedure IF EXISTS `update_short_link`;

DELIMITER $$

CREATE PROCEDURE `update_short_link` (
    IN id_value BIGINT,
    IN shorted_link_value TEXT,
    IN original_link_value TEXT
)
BEGIN

    UPDATE short_link
    SET shorted_link = shorted_link_value,
        original_link = original_link_value
    WHERE id = id_value;

END$$

DELIMITER ;

# Get short link by original url and email
USE `Quily`;
DROP procedure IF EXISTS `get_short_link_by_original_link_and_email`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE `get_short_link_by_original_link_and_email`(IN original_link_value TEXT,
IN email_value VARCHAR(255))
BEGIN
    SELECT * FROM Quily.short_link where original_link = original_link_value and user_email = email_value;
END$$

DELIMITER ;

USE `Quily`;
DROP PROCEDURE IF EXISTS `get_short_link_by_original_link_custom_alias_and_email`;

DELIMITER $$

CREATE PROCEDURE `get_short_link_by_original_link_custom_alias_and_email`(
    IN original_link_value TEXT,
    IN shorted_link_value TEXT,
    IN email_value VARCHAR(255)
)
BEGIN
    SELECT *
    FROM Quily.short_link
    WHERE (original_link = original_link_value AND user_email = email_value)
       OR shorted_link = shorted_link_value;
END$$

DELIMITER ;



