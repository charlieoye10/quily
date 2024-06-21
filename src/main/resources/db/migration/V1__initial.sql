CREATE TABLE users
(
    email     VARCHAR(255) PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    is_active BOOLEAN      NOT NULL
);

CREATE TABLE short_link
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       VARCHAR(255) NOT NULL,
    original_link TEXT         NOT NULL,
    shorted_link  TEXT         NOT NULL,
    creation_date VARCHAR(255) NOT NULL,
    expiry_date   VARCHAR(255) NOT NULL,
    is_active     BOOLEAN
);

CREATE TABLE key_indices
(
    id     INT,
    index1 INT,
    index2 INT,
    index3 INT,
    index4 INT,
    index5 INT,
    index6 INT
);

CREATE TABLE EmailConfirmationToken
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    confirmation_token VARCHAR(1000) NOT NULL,
    user_email         VARCHAR(255)  NOT NULL,
    created_time       VARCHAR(255)  NOT NULL
);

INSERT INTO key_indices VALUES (1, 0, 0, 0, 0, 0, 0)