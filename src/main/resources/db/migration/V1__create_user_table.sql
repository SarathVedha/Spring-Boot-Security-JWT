CREATE TABLE users
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_name     VARCHAR(255)                            NOT NULL,
    user_email    VARCHAR(255)                            NOT NULL,
    user_password VARCHAR(255)                            NOT NULL,
    user_role     VARCHAR(255)                            NOT NULL,
    created_date  TIMESTAMP,
    updated_date  TIMESTAMP,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_useremail UNIQUE (user_email);