# Get short link by short url
USE `Quily`;
DROP procedure IF EXISTS `get_short_link_by_url`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE `get_short_link_by_url`(IN url TEXT)
BEGIN
    SELECT * FROM Quily.short_link where shorted_link like  concat('%',url);
END$$

DELIMITER ;

# Create short link
USE `Quily`;

DROP PROCEDURE IF EXISTS `create_short_link`;

DELIMITER $$

CREATE PROCEDURE `create_short_link` (
    IN p_user_id VARCHAR(255),
    IN p_original_link TEXT,
    IN p_shorted_link TEXT,
    IN p_creation_date VARCHAR(255),
    IN p_expiry_date VARCHAR(255),
    IN p_is_active BOOLEAN,
    IN p_link_to_compare VARCHAR(255)
)
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM `short_link`
        WHERE `user_id` = p_user_id
          AND `original_link` LIKE p_link_to_compare
    ) THEN
        INSERT INTO `short_link` (
            `user_id`,
            `original_link`,
            `shorted_link`,
            `creation_date`,
            `expiry_date`,
            `is_active`
        ) VALUES (
                     p_user_id,
                     p_original_link,
                     p_shorted_link,
                     p_creation_date,
                     p_expiry_date,
                     p_is_active
                 );
    END IF;
END$$

DELIMITER ;