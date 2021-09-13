CREATE TABLE `user` (
  `login_id` VARCHAR(100) NOT NULL COMMENT 'login id',
  `password` VARCHAR(255) NOT NULL COMMENT '암호회된 password',
  PRIMARY KEY (`login_id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
  
  CREATE TABLE `user_role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `login_id` VARCHAR(100) NOT NULL COMMENT 'login id fk',
  `role_name` VARCHAR(100) NOT NULL COMMENT 'role 이름 ROLE_ 로 시작하는 값이어야 한다.',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`login_id`) REFERENCES `user` (`login_id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE emoji_category
(
    `id`    INT            NOT NULL    AUTO_INCREMENT, 
    `name`  VARCHAR(50)    NOT NULL, 
     PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE emoji
(
    `id`           INT            NOT NULL    AUTO_INCREMENT, 
    `text`         VARCHAR(50)    NOT NULL, 
    `color`        VARCHAR(50)    NOT NULL, 
    `category_id`  INT            NOT NULL, 
     PRIMARY KEY (`id`),
     FOREIGN KEY (`category_id`) REFERENCES emoji_category (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE diary
(
    `id`           INT             NOT NULL    AUTO_INCREMENT, 
    `content`      TEXT            NULL, 
    `emoji_id`     INT             NOT NULL, 
    `login_id`     VARCHAR(100)    NOT NULL, 
    `create_date`  DATETIME        NOT NULL, 
     PRIMARY KEY (`id`),
     FOREIGN KEY (`login_id`) REFERENCES `user` (`login_id`),
     FOREIGN KEY (`emoji_id`) REFERENCES emoji (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE file
(
    `id`     INT             NOT NULL    AUTO_INCREMENT, 
    `file_path`   VARCHAR(100)    NOT NULL, 
    `owner`  VARCHAR(100)    NOT NULL, 
	`content_type` varchar(50) NOT NULL,
     PRIMARY KEY (`id`),
     FOREIGN KEY (`owner`) REFERENCES user (`login_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
