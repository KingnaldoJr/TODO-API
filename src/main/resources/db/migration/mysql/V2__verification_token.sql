CREATE TABLE verification_token (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL,
    token VARCHAR(36) NOT NULL,
    expiry_date DATETIME NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX fk_verification_token_id_idx (id ASC) VISIBLE,
    UNIQUE INDEX fk_verification_token_token_idx (token ASC) VISIBLE,
    CONSTRAINT fk_verification_token_user
        FOREIGN KEY (user_id) REFERENCES user(id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
) ENGINE = InnoDB;

ALTER TABLE user
    ADD COLUMN enabled BOOLEAN NOT NULL DEFAULT false
    AFTER phone;