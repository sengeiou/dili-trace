DROP TABLE IF EXISTS sys_config;
CREATE TABLE `sys_config`(  
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `instructions` VARCHAR(50) COMMENT '说明',
  `opt_type` VARCHAR(50) COMMENT '配置类型',
  `opt_category` VARCHAR(50) COMMENT '配置分类',
  `opt_value` VARCHAR(200) COMMENT '配置参数',
  PRIMARY KEY (`id`)
) CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

DELETE FROM sys_config WHERE opt_type = 'statisticBaseUser' AND opt_category IN ('bill','trade');
INSERT INTO `sys_config` ( `instructions`, `opt_type`, `opt_category`, `opt_value`) VALUES('展示大屏用户基数设定','statisticBaseUser','trade','0');
INSERT INTO `sys_config` ( `instructions`, `opt_type`, `opt_category`, `opt_value`) VALUES('展示大屏用户基数设定','statisticBaseUser','bill','0');
