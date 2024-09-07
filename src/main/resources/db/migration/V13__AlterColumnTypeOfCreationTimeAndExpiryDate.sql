
UPDATE Quily.short_link
SET creation_date = STR_TO_DATE(creation_date, '%h:%i%p on %d %M %Y')
where creation_date  LIKE '% %:% % on % % %';

ALTER TABLE Quily.short_link
MODIFY expiry_date VARCHAR(255) NULL;

UPDATE Quily.short_link
SET expiry_date = NULL
WHERE expiry_date = '' OR expiry_date NOT like '%-%-%';

UPDATE Quily.short_link
SET expiry_date = STR_TO_DATE(expiry_date, '%Y-%m-%d')
WHERE expiry_date IS NOT NULL AND expiry_date <> '' AND expiry_date like '%Y-%m-%d';


ALTER TABLE Quily.short_link
MODIFY COLUMN creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE Quily.short_link
MODIFY COLUMN expiry_date TIMESTAMP DEFAULT NULL;

ALTER TABLE Quily.short_link
ADD COLUMN update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL;


DELIMITER ;

# Create short link
USE `Quily`;

DROP PROCEDURE IF EXISTS `create_short_link`;

DELIMITER $$

CREATE PROCEDURE `create_short_link` (
    IN p_user_id VARCHAR(255),
    IN p_original_link TEXT,
    IN p_shorted_link TEXT,
    IN p_expiry_date TIMESTAMP,
    IN p_is_active BOOLEAN,
    IN p_link_to_compare TEXT
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
            `update_time`
        ) VALUES (
                     p_user_id,
                     p_original_link,
                     p_shorted_link,
                     NOW(),
                     p_expiry_date,
                     p_is_active,
                     NOW()
                 );
    END IF;
END$$

DELIMITER ;

USE `Quily`;
DROP procedure IF EXISTS `get_short_links`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE get_short_links(
    IN page_number INT,
    IN page_size INT,
    IN email VARCHAR(255)
)
BEGIN
    DECLARE offset INT;
    SET offset = (page_number - 1) * page_size;
    SELECT *
    FROM short_link
    WHERE user_email = email
    ORDER BY update_time DESC
    LIMIT page_size OFFSET offset;

END$$

DELIMITER ;