CREATE TABLE tokens
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    token        VARCHAR(1024)                           NOT NULL,
    token_type   VARCHAR(255),
    expired      BOOLEAN                                 NOT NULL,
    revoked      BOOLEAN                                 NOT NULL,
    created_date TIMESTAMP,
    updated_date TIMESTAMP,
    user_id      BIGINT,
    CONSTRAINT pk_tokens PRIMARY KEY (id)
);

ALTER TABLE tokens
    ADD CONSTRAINT FK_TOKENS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);