
ALTER TABLE `user`   
  ADD COLUMN `is_push` TINYINT(1) DEFAULT -1   NULL  COMMENT '上报标志位，1待上报，-1无需上报' ;



UPDATE `user` u
SET u.`is_push`=1
WHERE  u.`yn`=1
AND u.`validate_state`=40;