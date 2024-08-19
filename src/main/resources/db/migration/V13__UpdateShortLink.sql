USE `Quily`;
DROP procedure IF EXISTS `update_short_link`;

DELIMITER $$

CREATE PROCEDURE `update_short_link` (
    IN id_value BIGINT,
    IN shorted_link_value TEXT,
    IN original_link_value TEXT
)
BEGIN
    DECLARE v_rows_updated INT;

    UPDATE short_link
    SET shorted_link = shorted_link_value,
        original_link = original_link_value
    WHERE id = id_value;

END$$

DELIMITER ;