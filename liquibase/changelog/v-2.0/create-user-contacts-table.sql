CREATE SEQUENCE user_contacts_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS user_contacts (
     id BIGSERIAL PRIMARY KEY,
     telegram_id VARCHAR(255),
     mobile_phone VARCHAR(20)
);
