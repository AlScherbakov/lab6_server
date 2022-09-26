CREATE TABLE IF NOT EXISTS users
(
    id       bigint PRIMARY KEY,
    username text,
    password text
);

CREATE SEQUENCE IF NOT EXISTS users_ids;

INSERT INTO users
VALUES (nextval('users_ids'), 'Alex', sha224('1'));
