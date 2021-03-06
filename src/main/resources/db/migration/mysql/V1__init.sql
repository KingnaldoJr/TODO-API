ALTER DATABASE todo DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;

CREATE TABLE user (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(205) NOT NULL,
    password CHAR(60) NOT NULL,
    phone VARCHAR(20) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE,
    UNIQUE INDEX email_UNIQUE (email ASC) VISIBLE,
    UNIQUE INDEX phone (phone ASC) VISIBLE
) ENGINE = InnoDB;

CREATE TABLE todo (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id BIGINT UNSIGNED NOT NULL,
    title VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE,
    INDEX fk_todo_user_idx (user_id ASC) VISIBLE,
    CONSTRAINT fk_todo_user
        FOREIGN KEY (user_id) REFERENCES user(id)
        ON DELETE CASCADE
        ON UPDATE NO ACTION
) ENGINE = InnoDB;

CREATE TABLE task (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255) NULL,
    date DATE NULL,
    PRIMARY KEY (id),
    UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE
) ENGINE = InnoDB;

CREATE TABLE todo_task (
    todo_id BIGINT UNSIGNED NOT NULL,
    task_id BIGINT UNSIGNED NOT NULL,
    INDEX fk_todo_task_todo_idx (todo_id ASC) VISIBLE,
    INDEX fk_todo_task_task_idx (task_id ASC) VISIBLE,
    CONSTRAINT fk_todo_task_todo
        FOREIGN KEY (todo_id) REFERENCES todo(id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION,
    CONSTRAINT fk_todo_task_task
        FOREIGN KEY (task_id) REFERENCES task(id)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
) ENGINE = InnoDB;