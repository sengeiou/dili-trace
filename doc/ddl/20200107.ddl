ALTER TABLE `register_bill` MODIFY COLUMN `handle_result`  varchar(3000);
ALTER TABLE `register_bill` MODIFY COLUMN `handle_result_url`  varchar(1000);

ALTER TABLE `register_bill` MODIFY COLUMN `origin_certifiy_url`  varchar(1000);
ALTER TABLE `register_bill` MODIFY COLUMN `detect_report_url`  varchar(1000);


ALTER TABLE `code_generate` DROP COLUMN `suffix`  ;
ALTER TABLE `code_generate` DROP COLUMN `code`  ;
ALTER TABLE `code_generate` ADD COLUMN `pattern`  varchar(20) NULL COMMENT '模式';
ALTER TABLE `code_generate` ADD COLUMN `prefix`  varchar(20) NULL COMMENT '前缀';
ALTER TABLE `user` ADD COLUMN `is_delete`  BIGINT default 0 COMMENT '是否删除';



ALTER TABLE usual_address ADD CONSTRAINT usual_address_unique UNIQUE KEY (address_id,`type`);
DROP INDEX unique_phone on `user`;
DROP INDEX unique_cardno on `user`;

ALTER TABLE `user` ADD CONSTRAINT user_unique_phone UNIQUE KEY (phone,`is_delete`);
ALTER TABLE `user` ADD CONSTRAINT user_unique_cardno UNIQUE KEY (card_no,`is_delete`);






CREATE TABLE `user_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT 'ID',  
  `name` varchar(30) NOT NULL COMMENT '名称',
  `phone` varchar(15) NOT NULL COMMENT '手机号',
  `card_no` varchar(20) NOT NULL COMMENT '身份证号',
  `addr` varchar(50) NOT NULL COMMENT '地址',
  `card_no_front_url` varchar(100) DEFAULT NULL COMMENT '身份证正面',
  `card_no_back_url` varchar(100) DEFAULT NULL COMMENT '身份证反面',
  `tally_area_nos` varchar(60) NOT NULL COMMENT '理货区号',
  `business_license_url` varchar(100) DEFAULT NULL COMMENT '营业执照URL',
  `sales_city_id` bigint(20) DEFAULT NULL COMMENT '销售城市ID',
  `sales_city_name` varchar(20) DEFAULT NULL COMMENT '销售城市名称',
  `state` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:启用 0：禁用',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `user_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:业户 2：政府人员',
  `yn` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 -1：删除',
  `user_plates` varchar(800) NULL COMMENT '车牌',
  `plate_amount` int NOT NULL DEFAULT 0 COMMENT '车牌数量',
  `version` tinyint(4) NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


INSERT INTO user_history (user_id,name,phone,card_no,addr,card_no_front_url,card_no_back_url
,tally_area_nos,business_license_url,sales_city_id,sales_city_name,state,`password`,user_type
,yn,user_plates,plate_amount,`version`,created,modified)
SELECT u2.id,u2.name,u2.phone,u2.card_no,u2.addr,u2.card_no_front_url,u2.card_no_back_url 
,u2.tally_area_nos,u2.business_license_url,u2.sales_city_id,u2.sales_city_name,u2.state,u2.password,u2.user_type
,u2.yn,up.plates,IFNULL(up.cnt,0),u2.version,u2.created,u2.modified FROM `user` u2 
LEFT JOIN
(SELECT GROUP_CONCAT(plate) AS plates,count(1) AS cnt,user_id FROM user_plate up GROUP BY user_id)
 up ON u2.id=up.user_id;
 