ALTER TABLE short_link CHANGE user_id user_email VARCHAR(255) NOT NULL;

#user_id to user_email
USE `Quily`;

DROP PROCEDURE IF EXISTS `create_short_link`;

DELIMITER $$

CREATE PROCEDURE `create_short_link` (
    IN p_user_email VARCHAR(255),
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
        WHERE `user_email` = p_user_email
          AND `original_link` LIKE p_link_to_compare
    ) THEN
        INSERT INTO `short_link` (`user_email`,
                                  `original_link`,
                                  `shorted_link`,
                                  `creation_date`,
                                  `expiry_date`,
                                  `is_active`
        ) VALUES (p_user_email,
                  p_original_link,
                  p_shorted_link,
                  p_creation_date,
                  p_expiry_date,
                  p_is_active
                 );
    END IF;
END$$

DELIMITER ;