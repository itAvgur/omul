CREATE TABLE auth.users
(
    user_id   SERIAL PRIMARY KEY NOT NULL,
    login     VARCHAR(100)       NOT NULL,
    linked_id INT                NOT NULL,
    subsystem VARCHAR(25)        NOT NULL,
    password  VARCHAR(100)       NOT NULL,
    enabled   BOOLEAN            NOT NULL DEFAULT FALSE
);
CREATE INDEX users_login_idx ON auth.users (login);