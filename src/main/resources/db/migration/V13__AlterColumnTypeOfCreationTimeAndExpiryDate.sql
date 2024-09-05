
UPDATE Quily.short_link
SET creation_date = STR_TO_DATE(creation_date, '%h:%i%p on %d %M %Y')
where creation_date <> '';

ALTER TABLE Quily.short_link
MODIFY expiry_date VARCHAR(255) NULL;

UPDATE Quily.short_link
SET expiry_date = NULL
WHERE expiry_date = '';

UPDATE Quily.short_link
SET expiry_date = STR_TO_DATE(expiry_date, '%Y-%m-%d')
WHERE expiry_date IS NOT NULL AND expiry_date <> '';


ALTER TABLE short_link
MODIFY COLUMN creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE short_link
MODIFY COLUMN expiry_date TIMESTAMP DEFAULT NULL;

DELIMITER ;

# Create short link
USE `Quily`;

DROP PROCEDURE IF EXISTS `create_short_link`;

DELIMITER $$

CREATE PROCEDURE `create_short_link` (
    IN p_user_id VARCHAR(255),
    IN p_original_link TEXT,
    IN p_shorted_link TEXT,
    IN p_expiry_date DATE,
    IN p_is_active BOOLEAN,
    IN p_link_to_compare TEXT
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
                     NOW(),
                     p_expiry_date,
                     p_is_active
                 );
    END IF;
END$$

DELIMITER ;