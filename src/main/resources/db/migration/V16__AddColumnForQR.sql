ALTER TABLE Quily.short_link
ADD COLUMN is_qr_created TINYINT DEFAULT 0;

USE `Quily`;

DROP PROCEDURE IF EXISTS `create_short_link`;

DELIMITER $$

CREATE PROCEDURE `create_short_link` (
    IN p_user_id VARCHAR(255),
    IN p_original_link TEXT,
    IN p_shorted_link TEXT,
    IN p_expiry_date TIMESTAMP,
    IN p_is_active BOOLEAN,
    IN p_link_to_compare TEXT,
    IN p_is_qr_created TINYINT
)
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM `short_link`
        WHERE `user_email` = p_user_id
          AND `original_link` LIKE p_link_to_compare
    ) THEN
        INSERT INTO `short_link` (
            `user_email`,
            `original_link`,
            `shorted_link`,
            `creation_date`,
            `expiry_date`,
            `is_active`,
            `update_time`,
            `is_qr_created`
        ) VALUES (
                     p_user_id,
                     p_original_link,
                     p_shorted_link,
                     NOW(),
                     p_expiry_date,
                     p_is_active,
                     NOW(),
                     p_is_qr_created
                 );
    END IF;
END$$

DELIMITER ;