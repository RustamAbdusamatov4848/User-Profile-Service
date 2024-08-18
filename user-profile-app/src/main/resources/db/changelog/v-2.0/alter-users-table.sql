ALTER TABLE users
    ADD COLUMN user_contacts_id BIGINT;

ALTER TABLE users
    ADD CONSTRAINT fk_user_contacts
        FOREIGN KEY (user_contacts_id)
            REFERENCES user_contacts (id);
