ALTER TABLE `user_history` DROP COLUMN `user_type`;
ALTER TABLE `register_bill` ADD COLUMN `creation_source`  int default 10 COMMENT '数据创建来源';
