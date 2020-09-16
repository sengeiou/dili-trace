drop table if exists message_config;
CREATE TABLE `message_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operation` tinyint(4) DEFAULT NULL COMMENT '10:商户注册提交;11:商户注册审核通过;12:商户注册审核未通过;20:提交报备;21:报备审核通过;22:报备审核未通过;23:报备审核退回;30:进门审核;40:卖家下单;50:买家下单;51:卖家确认订单',
  `message_flag` char(1) DEFAULT NULL COMMENT '是否发站内信',
  `sms_flag` char(1) DEFAULT NULL COMMENT '是否发短信',
  `wechat_flag` char(1) DEFAULT NULL COMMENT '是否发微信',
  `sms_scene_code` varchar(20) DEFAULT NULL COMMENT '短信中心sceneCode',
  `wechat_template_id` varchar(50) DEFAULT NULL COMMENT '微信模板id',
  `event_message_title` varchar(50) DEFAULT NULL COMMENT '站内消息标题',
  `event_message_content` varchar(200) DEFAULT NULL COMMENT '站内消息内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists sms_message;
CREATE TABLE `sms_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source_business_type` tinyint(4) DEFAULT NULL COMMENT '关联业务类型 用户 10 报备 20 交易 30',
  `source_business_id` bigint(20) DEFAULT NULL COMMENT '关联业务单据id',
  `receive_phone` varchar(20) DEFAULT NULL COMMENT '接收短信手机号码',
  `send_reason` tinyint(4) DEFAULT NULL,
  `result_code` varchar(50) DEFAULT NULL COMMENT '短信发送返回结果码',
  `result_info` varchar(2000) DEFAULT NULL COMMENT '短信发送返回结果信息',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists user_store;
CREATE TABLE `user_store` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `store_name` varchar(100) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `INDEX_USER_ID` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists trade_push_log;
CREATE TABLE `trade_push_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trade_detail_id` bigint(20) DEFAULT NULL COMMENT '批次主键',
  `log_type` tinyint(4) DEFAULT NULL COMMENT '0:下架 1:上架',
  `product_name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `operation_weight` decimal(10,3) DEFAULT NULL COMMENT '上下架重量',
  `order_type` tinyint(4) DEFAULT NULL COMMENT '0：报备单 10：交易单',
  `order_id` bigint(20) DEFAULT NULL COMMENT '单据主键id。报备单id或者交易单id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '商户 id',
  `product_stock_id` bigint(20) DEFAULT NULL COMMENT '库存 id',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `operation_reason` varchar(200) DEFAULT NULL,
  `order_code` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `INDEX_TRADE_DETAIL_ID` (`user_id`,`trade_detail_id`),
  KEY `INDEX_USER_ID` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table trade_detail add column pushaway_weight decimal(10,3) default 0;

alter table trade_detail add column soft_weight decimal(10,3) default 0;



DROP TABLE IF EXISTS applets_config;
CREATE TABLE `applets_config` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `app_name` VARCHAR(50) DEFAULT NULL COMMENT '小程序名称',
  `app_id` VARCHAR(50) DEFAULT NULL COMMENT '小程序id',
  `app_secret` VARCHAR(100) DEFAULT NULL COMMENT '小程序Secret',
  `access_token` VARCHAR(1000) DEFAULT NULL COMMENT '小程序accessToken',
  `access_token_expires_in` INT(10) DEFAULT NULL COMMENT '小程序accessToken有效时间',
  `access_token_update_time` DATETIME DEFAULT NULL COMMENT '小程序accessToken更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `applets_config` CHANGE `app_name` `app_name` VARCHAR(50) CHARACTER SET utf8mb4 DEFAULT NULL;


insert into `applets_config` ( `app_name`, `app_id`, `app_secret`, `access_token`, `access_token_expires_in`, `access_token_update_time`) 
values('地利溯源小程序','wx9c2027ae603a1bf3','1f81c9ebeaccf9750e9c5512272291ff','36_ilrXqcTOTViwq2Vb5Hp5fbbu0tuXYtoDDF-KMZO6Yt4SO09vVQEsUIMQlnp8Eqbtf3uGuL4V8gtlq1V1wJ5eU1peL8SJvNIAhYz0NHGWqugxKBQctn6amiWuYAxTNUAaub8odZlw0dFszFYLDYKeAIAHCO','7200','2020-08-15 16:30:00');





ALTER TABLE `user`   
  ADD COLUMN `open_id` VARCHAR(50) NULL  COMMENT '微信openId',
  ADD COLUMN `confirm_date` DATETIME NULL  COMMENT '提示微信绑定，确认不再弹出日期';

  
insert  into `message_config`(`id`,`operation`,`message_flag`,`sms_flag`,`wechat_flag`,`sms_scene_code`,`wechat_template_id`,`event_message_title`,`event_message_content`) values (1,10,'1','0','0','','','商户审核提醒','{0}，提交资料审核申请，请及时审核');
insert  into `message_config`(`id`,`operation`,`message_flag`,`sms_flag`,`wechat_flag`,`sms_scene_code`,`wechat_template_id`,`event_message_title`,`event_message_content`) values (2,11,'0','0','0','passRegistered','','','');
insert  into `message_config`(`id`,`operation`,`message_flag`,`sms_flag`,`wechat_flag`,`sms_scene_code`,`wechat_template_id`,`event_message_title`,`event_message_content`) values (3,12,'0','0','0','failedRegistration','','','');
insert  into `message_config`(`id`,`operation`,`message_flag`,`sms_flag`,`wechat_flag`,`sms_scene_code`,`wechat_template_id`,`event_message_title`,`event_message_content`) values (4,20,'1','0','0','','','进场报备提醒','{0}，提交进场报备申请，请及时审核。');
insert  into `message_config`(`id`,`operation`,`message_flag`,`sms_flag`,`wechat_flag`,`sms_scene_code`,`wechat_template_id`,`event_message_title`,`event_message_content`) values (5,21,'0','0','0','passReport','','','');
insert  into `message_config`(`id`,`operation`,`message_flag`,`sms_flag`,`wechat_flag`,`sms_scene_code`,`wechat_template_id`,`event_message_title`,`event_message_content`) values (6,22,'0','0','0','failedReport','','','');
insert  into `message_config`(`id`,`operation`,`message_flag`,`sms_flag`,`wechat_flag`,`sms_scene_code`,`wechat_template_id`,`event_message_title`,`event_message_content`) values (7,23,'0','0','0','returnReport','','','');
insert  into `message_config`(`id`,`operation`,`message_flag`,`sms_flag`,`wechat_flag`,`sms_scene_code`,`wechat_template_id`,`event_message_title`,`event_message_content`) values (9,40,'1','0','0','','','订单交易成功','订单编号:{0}完成交易');
insert  into `message_config`(`id`,`operation`,`message_flag`,`sms_flag`,`wechat_flag`,`sms_scene_code`,`wechat_template_id`,`event_message_title`,`event_message_content`) values (10,50,'0','0','0','tradeOrder','','','');
insert  into `message_config`(`id`,`operation`,`message_flag`,`sms_flag`,`wechat_flag`,`sms_scene_code`,`wechat_template_id`,`event_message_title`,`event_message_content`) values (11,51,'0','0','0','tradeConfirm','','','');





ALTER TABLE `user` CHANGE `name` `name` VARCHAR(36) CHARACTER SET utf8mb4 NOT NULL;
ALTER TABLE `user_login_history` CHANGE `user_name` `user_name` VARCHAR(36) CHARACTER SET utf8mb4 DEFAULT NULL;
ALTER TABLE `user_qr_history` CHANGE `user_name` `user_name` VARCHAR(36) CHARACTER SET utf8mb4 DEFAULT NULL;
ALTER TABLE `user_store` CHANGE `store_name` `store_name` VARCHAR(100) CHARACTER SET utf8mb4 DEFAULT NULL;

ALTER TABLE `trade_detail` CHANGE `buyer_name` `buyer_name` VARCHAR(36) CHARACTER SET utf8mb4 DEFAULT NULL;
ALTER TABLE `trade_detail` CHANGE `seller_name` `seller_name` VARCHAR(36) CHARACTER SET utf8mb4 DEFAULT NULL;
ALTER TABLE `trade_order` CHANGE `buyer_name` `buyer_name` VARCHAR(36) CHARACTER SET utf8mb4 DEFAULT NULL;
ALTER TABLE `trade_order` CHANGE `seller_name` `seller_name` VARCHAR(36) CHARACTER SET utf8mb4 DEFAULT NULL;
ALTER TABLE `trade_request` CHANGE `buyer_name` `buyer_name` VARCHAR(36) CHARACTER SET utf8mb4 DEFAULT NULL;
ALTER TABLE `trade_request` CHANGE `seller_name` `seller_name` VARCHAR(36) CHARACTER SET utf8mb4 DEFAULT NULL;




INSERT INTO user_store(user_id,store_name,modified)
SELECT u.`id`,u.`name`,CURRENT_TIMESTAMP FROM `user` u;

-- patch 店铺名
SET @i=0;
UPDATE user_store u,
(SELECT s.id,s.user_id,s.store_name,CONCAT(s.store_name,'-',@i:=@i+1) new_store_name FROM user_store s
JOIN (SELECT u.store_name NAME FROM user_store u 
GROUP BY u.store_name
HAVING COUNT(0)>1) tmp ON s.store_name=tmp.name
LEFT JOIN (SELECT u.id FROM user_store u 
GROUP BY u.store_name) n ON n.id=s.id
WHERE n.id IS NULL) tmp
SET u.store_name=tmp.new_store_name
WHERE u.id=tmp.id;