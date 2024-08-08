CREATE SEQUENCE users_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS users (
         id BIGSERIAL primary key,
         first_name VARCHAR(50),
         last_name VARCHAR(50),
         email VARCHAR(255) UNIQUE
);
