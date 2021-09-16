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
  UNIQUE(`login_id`, `role_name`),
  FOREIGN KEY (`login_id`) REFERENCES `user` (`login_id`) ON DELETE CASCADE ON UPDATE CASCADE
)  ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE emoji_category
(
    `id`    INT            NOT NULL    AUTO_INCREMENT, 
    `name`  VARCHAR(50)    NOT NULL, 
    `color`        VARCHAR(50)    NOT NULL, 
     PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE emoji
(
    `id`           INT            NOT NULL    AUTO_INCREMENT, 
    `text`         VARCHAR(50)    NOT NULL, 
    `category_id`  INT            NOT NULL, 
     PRIMARY KEY (`id`),
     FOREIGN KEY (`category_id`) REFERENCES emoji_category (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE diary
(
    `id`           INT             NOT NULL    AUTO_INCREMENT, 
    `content`      TEXT            NULL, 
    `emoji_id`     INT             NOT NULL, 
    `owner`     VARCHAR(100)       NOT NULL, 
    `create_date`  DATETIME        NOT NULL, 
     PRIMARY KEY (`id`),
     FOREIGN KEY (`writer`) REFERENCES `user` (`login_id`) ON DELETE CASCADE ON UPDATE CASCADE,
     FOREIGN KEY (`emoji_id`) REFERENCES emoji (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE file
(
    `id`     INT             	  NOT NULL    AUTO_INCREMENT, 
    `file_path`   VARCHAR(100)    NOT NULL, 
    `owner`  	  VARCHAR(100)    NOT NULL, 
	`content_type` varchar(50) 	  NOT NULL,
    `url` 		  TEXT            NULL,
     PRIMARY KEY (`id`),
     FOREIGN KEY (`owner`) REFERENCES user (`login_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE voice
(
    `id`     INT             NOT NULL    AUTO_INCREMENT COMMENT '음성 id', 
    `name`   VARCHAR(100)    NOT NULL    COMMENT '음성명', 
    `owner`  VARCHAR(100)    NOT NULL, 
    PRIMARY KEY (id),
    UNIQUE(`owner`, `name`),
    FOREIGN KEY (owner) REFERENCES user (login_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE chat_mode
(
    `id`    INT            NOT NULL    AUTO_INCREMENT, 
    `name`  VARCHAR(50)    NOT NULL, 
     PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE chat_bot
(
    `id`             INT             NOT NULL    AUTO_INCREMENT, 
    `mode_id`        INT             NOT NULL, 
    `voice_id`       INT             NOT NULL, 
    `image_file_id`  INT             NULL, 
    `name`           VARCHAR(100)    NOT NULL, 
    `owner`          VARCHAR(100)    NOT NULL, 
     PRIMARY KEY (id),
     UNIQUE(`owner`, `name`),
     FOREIGN KEY (image_file_id) REFERENCES file (id) ON DELETE SET NULL ON UPDATE CASCADE,
     FOREIGN KEY (voice_id) REFERENCES voice (id) ON UPDATE CASCADE,
     FOREIGN KEY (mode_id) REFERENCES chat_mode (id) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE chat
(
    `id`           BIGINT         NOT NULL    AUTO_INCREMENT, 
    `bot_id`       INT            NOT NULL, 
    `text`         VARCHAR(50)    NOT NULL, 
    `is_bot`       INT            NOT NULL, 
    `create_date`  DATETIME       NOT NULL, 
    `owner`  VARCHAR(100)    	  NOT NULL,        
     PRIMARY KEY (id),
     FOREIGN KEY (owner) REFERENCES user (login_id) ON DELETE CASCADE ON UPDATE CASCADE,
     FOREIGN KEY (bot_id) REFERENCES chat_bot (id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

