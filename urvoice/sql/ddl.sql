CREATE TABLE `userDto` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `login_id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'login id',
  `password` VARCHAR(255) NOT NULL COMMENT '암호회된 password',
  PRIMARY KEY (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
  
  CREATE TABLE `user_role` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` INT(11) NOT NULL COMMENT 'userDto id fk',
  `role_name` VARCHAR(100) NOT NULL COMMENT 'role 이름 ROLE_ 로 시작하는 값이어야 한다.',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`)
  REFERENCES `userDto` (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8