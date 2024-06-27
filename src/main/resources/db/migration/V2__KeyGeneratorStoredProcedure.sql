# update key indices
USE `Quily`;
DROP procedure IF EXISTS `update_key_indices_only_if_db_indices_is_smaller`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE `update_key_indices_only_if_db_indices_is_smaller` (
    IN p_id INT,
    IN p_index1 INT,
    IN p_index2 INT,
    IN p_index3 INT,
    IN p_index4 INT,
    IN p_index5 INT,
    IN p_index6 INT
)
BEGIN
    DECLARE v_combined_new INT;
    DECLARE v_combined_old INT;
    DECLARE v_rows_updated INT;

    SET v_combined_new = (p_index6 * 100000 + p_index5 * 10000 + p_index4 * 1000 + p_index3 * 100 + p_index2 * 10 + p_index1);

    SET v_combined_old = (SELECT (index6 * 100000 + index5 * 10000 + index4 * 1000 + index3 * 100 + index2 * 10 + index1)
                          FROM key_indices
                          WHERE id = p_id);

    IF v_combined_new > v_combined_old THEN
        UPDATE key_indices
        SET index1 = p_index1,
            index2 = p_index2,
            index3 = p_index3,
            index4 = p_index4,
            index5 = p_index5,
            index6 = p_index6
        WHERE id = p_id;

        SET v_rows_updated = ROW_COUNT();

        SELECT v_rows_updated AS rows_updated;
    ELSE
        SELECT 0 AS rows_updated;
    END IF;
END$$

DELIMITER ;

# Get key indices
USE `Quily`;
DROP procedure IF EXISTS `get_key_indices_by_id`;

DELIMITER $$
USE `Quily`$$
CREATE PROCEDURE `get_key_indices_by_id` (IN key_id INT)
BEGIN
    SELECT * FROM Quily.key_indices WHERE id = key_id;
END$$

DELIMITER ;

