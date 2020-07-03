
CREATE TABLE `category` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`parent_id` bigint(20)  NULL COMMENT '上一级ID',
	`name` varchar(20)  NULL COMMENT '名称',
	`full_name` varchar(20)  NULL COMMENT '全名',
	`level` int(11) not NULL default 1 COMMENT '层级',
	`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `image_cert` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`bill_id` bigint(20)  NULL COMMENT '所属数据ID',
	`url` varchar(200)  NULL COMMENT '图片URL',
	`cert_type` int(11) not NULL COMMENT '图片类型',
	`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `trade_detail` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`parent_id` bigint(20)  NULL COMMENT '分销来源ID',
	`bill_id` bigint(20)  NOT NULL COMMENT '登记单ID',
	`checkin_record_id` bigint(20)  NULL COMMENT '进门ID',
	`checkout_record_id` bigint(20)  NULL COMMENT '进门ID',
	`checkin_status` int(11) NOT NULL COMMENT '进门状态',
	`checkout_status` int(11) NOT NULL COMMENT '出门状态',
	`sale_status` int(11) NOT NULL COMMENT '交易状态',
	`trade_type` int(11) NOT NULL COMMENT '交易类型',
	`buyer_id` bigint(20) NOT NULL COMMENT '买家ID',
	`buyer_name` varchar(20) NOT NULL COMMENT '买家姓名',
	`seller_id` bigint(20)  NULL COMMENT '卖家ID',
	`seller_name` varchar(20)  NULL COMMENT '卖家姓名',
	`product_name` varchar(50) NOT NULL COMMENT '商品名称',
	`stock_weight` decimal(10,3) NOT NULL default 0 COMMENT '库存重量',
	`total_weight` decimal(10,3) NOT NULL default 0 COMMENT '总重量',
	`weight_unit` int(11) NOT NULL default 10 COMMENT '重量单位',
	`status` int(11) NOT NULL default 0 COMMENT '交易单状态',
	`batch_stock_id` bigint(20)  NULL COMMENT '批ID',
	`trade_request_id` bigint(20)  NULL COMMENT '交易请求ID',
	`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO category (parent_id,name,full_name,`level`,created,modified) VALUES 
(NULL,'贝类','贝类',1,now(),now())
,(NULL,'淡水鱼','淡水鱼',1,now(),now())
,(NULL,'高档海鲜','高档海鲜',1,now(),now())
,(NULL,'海水鱼','海水鱼',1,now(),now())
,(NULL,'河鲜','河鲜',1,now(),now())
,(NULL,'虾类','虾类',1,now(),now())
,(NULL,'其他','其他',1,now(),now())
;
INSERT INTO category (parent_id,name,full_name,`level`,created,modified) VALUES 
(1,'白蛤','贝类,白蛤',2,now(),now())
,(1,'白玉贝','贝类,白玉贝',2,now(),now())
,(1,'北极贝','贝类,北极贝',2,now(),now())
,(1,'朝鲜蚌','贝类,朝鲜蚌',2,now(),now())
,(1,'蛏子','贝类,蛏子',2,now(),now())
,(1,'赤贝','贝类,赤贝',2,now(),now())
,(1,'大角螺','贝类,大角螺',2,now(),now())
,(1,'淡菜','贝类,淡菜',2,now(),now())
,(1,'刀贝','贝类,刀贝',2,now(),now())
,(1,'钉螺','贝类,钉螺',2,now(),now())
,(1,'肥贝','贝类,肥贝',2,now(),now())
,(1,'广州蚌仔','贝类,广州蚌仔',2,now(),now())
,(1,'蛤蜊','贝类,蛤蜊',2,now(),now())
,(1,'海蚌','贝类,海蚌',2,now(),now())
,(1,'海带花','贝类,海带花',2,now(),now())
,(1,'海瓜子','贝类,海瓜子',2,now(),now())
,(1,'海花','贝类,海花',2,now(),now())
,(1,'海螺','贝类,海螺',2,now(),now())
,(1,'河蚌','贝类,河蚌',2,now(),now())
,(1,'红心蛏','贝类,红心蛏',2,now(),now())
,(1,'花蛤','贝类,花蛤',2,now(),now())
,(1,'花螺','贝类,花螺',2,now(),now())
,(1,'黄贝','贝类,黄贝',2,now(),now())
,(1,'黄金贝','贝类,黄金贝',2,now(),now())
,(1,'黄金螺','贝类,黄金螺',2,now(),now())
,(1,'黄螺','贝类,黄螺',2,now(),now())
,(1,'扣贝','贝类,扣贝',2,now(),now())
,(1,'辣螺','贝类,辣螺',2,now(),now())
,(1,'老头蛤','贝类,老头蛤',2,now(),now())
,(1,'龙须草','贝类,龙须草',2,now(),now())
,(1,'螺蛳','贝类,螺蛳',2,now(),now())
,(1,'芒果贝','贝类,芒果贝',2,now(),now())
,(1,'毛贝','贝类,毛贝',2,now(),now())
,(1,'美国贝','贝类,美国贝',2,now(),now())
,(1,'牡蛎','贝类,牡蛎',2,now(),now())
,(1,'泥螺','贝类,泥螺',2,now(),now())
,(1,'鸟贝','贝类,鸟贝',2,now(),now())
,(1,'七彩贝','贝类,七彩贝',2,now(),now())
,(1,'青蛤','贝类,青蛤',2,now(),now())
,(1,'青口贝','贝类,青口贝',2,now(),now())
,(1,'扇贝','贝类,扇贝',2,now(),now())
,(1,'生蚝','贝类,生蚝',2,now(),now())
,(1,'湿海蛎','贝类,湿海蛎',2,now(),now())
,(1,'石头贝','贝类,石头贝',2,now(),now())
,(1,'石蟹','贝类,石蟹',2,now(),now())
,(1,'特大贝','贝类,特大贝',2,now(),now())
,(1,'天鹅蛋','贝类,天鹅蛋',2,now(),now())
,(1,'田螺','贝类,田螺',2,now(),now())
,(1,'文蛤','贝类,文蛤',2,now(),now())
,(1,'蜗牛','贝类,蜗牛',2,now(),now())
,(1,'西施贝','贝类,西施贝',2,now(),now())
,(1,'香螺','贝类,香螺',2,now(),now())
,(1,'小蚌','贝类,小蚌',2,now(),now())
,(1,'血蛤','贝类,血蛤',2,now(),now())
,(1,'一点红','贝类,一点红',2,now(),now())
,(1,'油蛤','贝类,油蛤',2,now(),now())
,(1,'月亮贝','贝类,月亮贝',2,now(),now())
,(1,'珍珠贝','贝类,珍珠贝',2,now(),now())
,(1,'芝麻螺','贝类,芝麻螺',2,now(),now())
,(1,'竹蛏','贝类,竹蛏',2,now(),now())
,(1,'象拔蚌','贝类,象拔蚌',2,now(),now())
,(1,'鲍鱼','贝类,鲍鱼',2,now(),now())
;
INSERT INTO category (parent_id,name,full_name,`level`,created,modified) VALUES 
(2,'草鱼','淡水鱼,草鱼',2,now(),now())
,(2,'花鲢','淡水鱼,花鲢',2,now(),now())
,(2,'白莲','淡水鱼,白莲',2,now(),now())
,(2,'鲫鱼','淡水鱼,鲫鱼',2,now(),now())
,(2,'鳊鱼','淡水鱼,鳊鱼',2,now(),now())
,(2,'黑鱼','淡水鱼,黑鱼',2,now(),now())
,(2,'鲤鱼','淡水鱼,鲤鱼',2,now(),now())
,(2,'青鱼','淡水鱼,青鱼',2,now(),now())
,(2,'湘鲫','淡水鱼,湘鲫',2,now(),now())
,(2,'鲈鱼','淡水鱼,鲈鱼',2,now(),now())
,(2,'桂鱼','淡水鱼,桂鱼',2,now(),now())
,(2,'江刺鱼','淡水鱼,江刺鱼',2,now(),now())
,(2,'白条','淡水鱼,白条',2,now(),now())
,(2,'罗非鱼','淡水鱼,罗非鱼',2,now(),now())
,(2,'河鳗','淡水鱼,河鳗',2,now(),now())
,(2,'江鳗','淡水鱼,江鳗',2,now(),now())
,(2,'鲶鱼','淡水鱼,鲶鱼',2,now(),now())
,(2,'红鮰','淡水鱼,红鮰',2,now(),now())
,(2,'黑鮰','淡水鱼,黑鮰',2,now(),now())
,(2,'洋花鱼','淡水鱼,洋花鱼',2,now(),now())
,(2,'鲻鱼','淡水鱼,鲻鱼',2,now(),now())
,(2,'太阳鱼','淡水鱼,太阳鱼',2,now(),now())
,(2,'鳕鱼','淡水鱼,鳕鱼',2,now(),now())
,(2,'牛尾鱼','淡水鱼,牛尾鱼',2,now(),now())
,(2,'中华鲟','淡水鱼,中华鲟',2,now(),now())
,(2,'鱼钩鱼','淡水鱼,鱼钩鱼',2,now(),now())
,(2,'鸭嘴鱼','淡水鱼,鸭嘴鱼',2,now(),now())
;
INSERT INTO category (parent_id,name,full_name,`level`,created,modified) VALUES
(3,'生态黄鱼','高档海鲜,生态黄鱼',2,now(),now())
,(3,'十八梅','高档海鲜,十八梅',2,now(),now())
,(3,'美国红鱼','高档海鲜,美国红鱼',2,now(),now())
,(3,'金鲳鱼','高档海鲜,金鲳鱼',2,now(),now())
,(3,'黑珍鲷','高档海鲜,黑珍鲷',2,now(),now())
,(3,'青斑','高档海鲜,青斑',2,now(),now())
,(3,'石斑','高档海鲜,石斑',2,now(),now())
,(3,'龙纹斑','高档海鲜,龙纹斑',2,now(),now())
,(3,'老虎斑','高档海鲜,老虎斑',2,now(),now())
,(3,'中华鲟','高档海鲜,中华鲟',2,now(),now())
,(3,'淡水小石斑','高档海鲜,淡水小石斑',2,now(),now())
,(3,'笋壳鱼','高档海鲜,笋壳鱼',2,now(),now())
,(3,'东星斑','高档海鲜,东星斑',2,now(),now())
,(3,'多宝鱼','高档海鲜,多宝鱼',2,now(),now())
,(3,'左口鱼','高档海鲜,左口鱼',2,now(),now())
,(3,'鲍鱼','高档海鲜,鲍鱼',2,now(),now())
,(3,'澳鲍','高档海鲜,澳鲍',2,now(),now())
,(3,'雪鱼','高档海鲜,雪鱼',2,now(),now())
,(3,'三文鱼','高档海鲜,三文鱼',2,now(),now())
,(3,'金枪鱼','高档海鲜,金枪鱼',2,now(),now())
,(3,'海鲈鱼','高档海鲜,海鲈鱼',2,now(),now())
,(3,'黄杉鱼','高档海鲜,黄杉鱼',2,now(),now())
,(3,'柳根鱼','高档海鲜,柳根鱼',2,now(),now())
,(3,'香鱼','高档海鲜,香鱼',2,now(),now())
,(3,'老鼠斑','高档海鲜,老鼠斑',2,now(),now())
,(3,'河豚','高档海鲜,河豚',2,now(),now())
,(3,'澳龙','高档海鲜,澳龙',2,now(),now())
,(3,'红龙','高档海鲜,红龙',2,now(),now())
,(3,'波士顿龙','高档海鲜,波士顿龙',2,now(),now())
,(3,'小青龙','高档海鲜,小青龙',2,now(),now())
,(3,'火龙','高档海鲜,火龙',2,now(),now())
,(3,'南非龙','高档海鲜,南非龙',2,now(),now())
,(3,'象拔蚌','高档海鲜,象拔蚌',2,now(),now())
,(3,'海参','高档海鲜,海参',2,now(),now())
,(3,'珍宝蟹','高档海鲜,珍宝蟹',2,now(),now())
,(3,'面包蟹','高档海鲜,面包蟹',2,now(),now())
,(3,'帝王蟹','高档海鲜,帝王蟹',2,now(),now())
,(3,'皇帝蟹','高档海鲜,皇帝蟹',2,now(),now())
,(3,'雪蟹','高档海鲜,雪蟹',2,now(),now())
,(3,'长脚蟹','高档海鲜,长脚蟹',2,now(),now())
,(3,'红花蟹','高档海鲜,红花蟹',2,now(),now())
,(3,'兰花蟹','高档海鲜,兰花蟹',2,now(),now())
,(3,'梭子蟹','高档海鲜,梭子蟹',2,now(),now())
,(3,'石蟹','高档海鲜,石蟹',2,now(),now())
,(3,'沙蟹','高档海鲜,沙蟹',2,now(),now())
,(3,'蓝蟹','高档海鲜,蓝蟹',2,now(),now())
,(3,'红毛蟹','高档海鲜,红毛蟹',2,now(),now())
,(3,'青蟹','高档海鲜,青蟹',2,now(),now())
,(3,'椰子蟹','高档海鲜,椰子蟹',2,now(),now())
,(3,'老虎蟹','高档海鲜,老虎蟹',2,now(),now())
;
INSERT INTO category (parent_id,name,full_name,`level`,created,modified) VALUES 
(4,'米鱼','海水鱼,米鱼',2,now(),now())
,(4,'龙头鱼','海水鱼,龙头鱼',2,now(),now())
,(4,'马面鱼','海水鱼,马面鱼',2,now(),now())
,(4,'龙利鱼','海水鱼,龙利鱼',2,now(),now())
,(4,'月亮鱼','海水鱼,月亮鱼',2,now(),now())
,(4,'刀鱼','海水鱼,刀鱼',2,now(),now())
,(4,'鲅鱼','海水鱼,鲅鱼',2,now(),now())
,(4,'青占鱼','海水鱼,青占鱼',2,now(),now())
,(4,'安康鱼','海水鱼,安康鱼',2,now(),now())
,(4,'墨鱼','海水鱼,墨鱼',2,now(),now())
,(4,'鱿鱼','海水鱼,鱿鱼',2,now(),now())
,(4,'章鱼','海水鱼,章鱼',2,now(),now())
,(4,'马头鱼','海水鱼,马头鱼',2,now(),now())
,(4,'海鳗','海水鱼,海鳗',2,now(),now())
,(4,'海虾','海水鱼,海虾',2,now(),now())
,(4,'带鱼','海水鱼,带鱼',2,now(),now())
,(4,'鲳鱼','海水鱼,鲳鱼',2,now(),now())
,(4,'鳓鱼','海水鱼,鳓鱼',2,now(),now())
,(4,'鲥鱼','海水鱼,鲥鱼',2,now(),now())
,(4,'黄花鱼','海水鱼,黄花鱼',2,now(),now())
,(4,'黄鲷鱼','海水鱼,黄鲷鱼',2,now(),now())
,(4,'黄菇鱼','海水鱼,黄菇鱼',2,now(),now())
,(4,'银鳕鱼','海水鱼,银鳕鱼',2,now(),now())
,(4,'鲽鱼','海水鱼,鲽鱼',2,now(),now())
,(4,'石斑鱼','海水鱼,石斑鱼',2,now(),now())
,(4,'金枪鱼','海水鱼,金枪鱼',2,now(),now())
,(4,'海鲈鱼','海水鱼,海鲈鱼',2,now(),now())
,(4,'秋刀鱼','海水鱼,秋刀鱼',2,now(),now())
,(4,'长寿鱼','海水鱼,长寿鱼',2,now(),now())
,(4,'虎头鱼','海水鱼,虎头鱼',2,now(),now())
,(4,'白菇鱼','海水鱼,白菇鱼',2,now(),now())
,(4,'梅童鱼','海水鱼,梅童鱼',2,now(),now())
,(4,'八爪鱼','海水鱼,八爪鱼',2,now(),now())
;
INSERT INTO category (parent_id,name,full_name,`level`,created,modified) VALUES 
(5,'黄鳝','河鲜,黄鳝',2,now(),now())
,(5,'小龙虾','河鲜,小龙虾',2,now(),now())
,(5,'甲鱼','河鲜,甲鱼',2,now(),now())
,(5,'泥鳅','河鲜,泥鳅',2,now(),now())
,(5,'牛蛙','河鲜,牛蛙',2,now(),now())
,(5,'江鳗','河鲜,江鳗',2,now(),now())
,(5,'河鳗','河鲜,河鳗',2,now(),now())
,(5,'白鳗','河鲜,白鳗',2,now(),now())
,(5,'巴西龟','河鲜,巴西龟',2,now(),now())
,(5,'草龟','河鲜,草龟',2,now(),now())
,(5,'鳄鱼龟','河鲜,鳄鱼龟',2,now(),now())
,(5,'湖蟹','河鲜,湖蟹',2,now(),now())
;
INSERT INTO category (parent_id,name,full_name,`level`,created,modified) VALUES 
(6,'黑虎虾','虾类,黑虎虾',2,now(),now())
,(6,'白虾','虾类,白虾',2,now(),now())
,(6,'南美白对虾','虾类,南美白对虾',2,now(),now())
,(6,'基围虾','虾类,基围虾',2,now(),now())
,(6,'河虾','虾类,河虾',2,now(),now())
,(6,'沼虾','虾类,沼虾',2,now(),now())
,(6,'竹节虾','虾类,竹节虾',2,now(),now())
,(6,'富贵虾','虾类,富贵虾',2,now(),now())
,(6,'牡丹虾','虾类,牡丹虾',2,now(),now())
,(6,'青虾','虾类,青虾',2,now(),now())
,(6,'明虾','虾类,明虾',2,now(),now())
,(6,'草虾','虾类,草虾',2,now(),now());
INSERT INTO category (parent_id,name,full_name,`level`,created,modified) VALUES 
(7,'鱼肚','其他,鱼肚',2,now(),now())
,(7,'八爪鱼','其他,八爪鱼',2,now(),now())
,(7,'鱿鱼','其他,鱿鱼',2,now(),now())
,(7,'蚕蛹','其他,蚕蛹',2,now(),now())
,(7,'叽咕','其他,叽咕',2,now(),now())
,(7,'海参','其他,海参',2,now(),now())
;

CREATE TABLE `batch_stock` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`preserve_type` int(11) NOT NULL  default 10 COMMENT '保存类型',
	`stock_weight` decimal(10,3) NOT NULL default 0 COMMENT '库存重量',
	`total_weight` decimal(10,3) NOT NULL default 0 COMMENT '总重量',
	`weight_unit` int(11) NOT NULL default 10 COMMENT '重量单位',
	`spec_name` varchar(20)  NULL  COMMENT '规格',
	`product_name` varchar(20) NOT NULL COMMENT '商品名称',
  	`product_id` bigint(20) NOT NULL,
   	`user_name` varchar(50) DEFAULT NULL COMMENT '业户姓名',
  	`user_id` bigint(20) DEFAULT NULL COMMENT '理货区用户ID',
	`brand_id` bigint(20) DEFAULT NULL COMMENT '品牌ID',
	`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `brand` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`brand_name` varchar(50)   NULL   COMMENT '品牌名称',
  	`user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
	`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

drop table `quality_trace_trade_bill`;
drop table `quality_trace_trade_bill_syncpoint`;
drop table `user_tally_area`;

drop table `check_sheet`;
drop table `check_sheet_detail`;

drop table `detect_record`;


ALTER TABLE `register_bill` ADD COLUMN  `weight_unit` int(11) NOT NULL default 1 COMMENT '重量单位';
ALTER TABLE `register_bill` ADD COLUMN  `verify_status` int(11) not null default 0 COMMENT '查验状态';
ALTER TABLE `register_bill` ADD COLUMN  `preserve_type` int(11) NOT NULL  default 10 COMMENT '保存类型';
ALTER TABLE `register_bill` ADD COLUMN  `verify_type` int(11)  NOT NULL  default 10 COMMENT '查验类型';
ALTER TABLE `register_bill` ADD COLUMN  `spec_name` varchar(20)  NULL  COMMENT '规格';
ALTER TABLE `register_bill` ADD COLUMN  `bill_type` int(11)  NOT NULL  default 10 COMMENT '报备类型';
ALTER TABLE `register_bill` ADD COLUMN  `truck_type` int(11)  NOT NULL  default 10 COMMENT '车类型';
ALTER TABLE `register_bill` ADD COLUMN  `brand_id` bigint(20)    NULL   COMMENT '品牌ID';
ALTER TABLE `register_bill` ADD COLUMN  `brand_name` varchar(50)   NULL   COMMENT '品牌名称';
ALTER TABLE `register_bill` ADD COLUMN  `reason` varchar(100)   NULL   COMMENT '原因';
ALTER TABLE `register_bill` ADD COLUMN  `yn` int(11)  NOT NULL  default 1 COMMENT '是否有效';
ALTER TABLE `register_bill` MODIFY COLUMN `weight` decimal(10,3)  NOT NULL default 0 COMMENT '重量';
ALTER TABLE `register_bill` MODIFY COLUMN plate varchar(15) NULL COMMENT '车牌';


ALTER TABLE `register_bill` DROP COLUMN `sales_type`;

ALTER TABLE `register_bill` DROP COLUMN `register_source`;

ALTER TABLE `register_bill` DROP COLUMN `trade_account`;
ALTER TABLE `register_bill` DROP COLUMN `trade_printing_card`;

ALTER TABLE `register_bill` DROP COLUMN `detect_report_url`;

ALTER TABLE `register_bill` DROP COLUMN `origin_certifiy_url`;
ALTER TABLE `register_bill` DROP COLUMN `exe_machine_no`;

ALTER TABLE `register_bill` DROP COLUMN `trade_type_id`;
ALTER TABLE `register_bill` DROP COLUMN `trade_type_name`;
ALTER TABLE `register_bill` DROP COLUMN `handle_result_url`;
ALTER TABLE `register_bill` DROP COLUMN `handle_result`;
ALTER TABLE `register_bill` DROP COLUMN `creation_source`;
ALTER TABLE `register_bill` DROP COLUMN `check_sheet_id`;
ALTER TABLE `register_bill` DROP COLUMN `separate_sales_record_id`;





ALTER TABLE `checkinout_record` ADD COLUMN  `inout_weight` decimal(10,3) not null default 0 COMMENT '进出门重量';
ALTER TABLE `checkinout_record` ADD COLUMN  `trade_detail_id` bigint not null default 0 COMMENT '分销ID';

ALTER TABLE `checkinout_record` DROP COLUMN `sales_weight`;
ALTER TABLE `checkinout_record` DROP COLUMN `seperate_sales_id`;

#2020年6月23日11点35分
ALTER TABLE `user` DROP INDEX user_unique_cardno;
ALTER TABLE `user` ADD COLUMN validate_state tinyint(2) DEFAULT 10 NOT NULL COMMENT '未实名10 待审核 20  审核未通过 30 审核通过 40';
ALTER TABLE `user` MODIFY COLUMN business_license_url varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '营业执照';
ALTER TABLE `user` ADD vocation_type TINYINT(2) NULL COMMENT '批发 10 农贸 20 团体 30 个人40 餐饮50 配送商 60';
#2020年7月1日
ALTER TABLE upstream ADD upORdown TINYINT(2) DEFAULT 10 NULL COMMENT '上游企业10 下游企业20';

CREATE TABLE event_message (
	id BIGINT(20) auto_increment NOT NULL,
	title varchar(20) NULL,
	overview varchar(50) NULL,
	content varchar(150) NULL,
	source_business_id BIGINT(20) NULL COMMENT '关联业务id',
	source_business_type_id BIGINT(20) NULL COMMENT '关联业务类型id',
	creator_id BIGINT(20) NULL,
	creator varchar(50) NULL,
	receiver_id BIGINT(20) NULL COMMENT '消息接收者id',
	receiver varchar(50) NULL,
	create_time TIMESTAMP DEFAULT now() NULL,
	read_flag TINYINT(1) DEFAULT 0 NULL COMMENT '已读标志 未读0 已读 1',
	CONSTRAINT event_message_PK PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci;

CREATE TABLE `register_bill_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NOT NULL,
  `code` varchar(20) NOT NULL COMMENT '编号',
  `sample_code` varchar(20) DEFAULT NULL COMMENT '采样编号',
  `name` varchar(50) DEFAULT NULL COMMENT '业户姓名',
  `id_card_no` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `addr` varchar(50) DEFAULT NULL COMMENT '地址',
  `phone` varchar(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL COMMENT '理货区用户ID',
  `plate` varchar(15)  NULL COMMENT '车牌',
  `state` tinyint(1) NOT NULL COMMENT '1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中，8.审核未通过',
  `sales_type` tinyint(1) DEFAULT NULL COMMENT '1.分销 2.全销',
  `product_name` varchar(20) NOT NULL COMMENT '商品名称',
  `product_id` bigint(20) NOT NULL,
  `origin_id` bigint(20) DEFAULT NULL COMMENT '产地ID',
  `origin_name` varchar(50) DEFAULT NULL COMMENT '产地',
  `weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '重量',
  `operator_id` bigint(20) DEFAULT NULL,
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人',
  `sample_source` tinyint(1) DEFAULT NULL COMMENT '1:采样检测 2:主动送检',
  `detect_state` tinyint(1) DEFAULT NULL COMMENT '默认null,1.合格 2.不合格 3.复检合格 4.复检不合格',
  `latest_detect_record_id` bigint(20) DEFAULT NULL COMMENT '检测记录ID',
  `latest_detect_operator` varchar(20) DEFAULT NULL COMMENT '检测人员',
  `latest_detect_time` timestamp NULL DEFAULT NULL COMMENT '检测时间',
  `latest_pd_result` varchar(100) DEFAULT NULL COMMENT '最新一次检测值',
  `version` tinyint(4) NOT NULL DEFAULT '0' COMMENT '版本号',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `upstream_id` bigint(20) DEFAULT NULL COMMENT '上游信息ID',
  `complete` int(11) DEFAULT NULL COMMENT '信息是否完整',
  `verify_status` int(11) NOT NULL DEFAULT '0' COMMENT '查验状态',
  `weight_unit` int(11) NOT NULL DEFAULT '1' COMMENT '重量单位',
  `preserve_type` int(11) NOT NULL DEFAULT '10' COMMENT '保存类型',
  `verify_type` int(11) NOT NULL DEFAULT '0' COMMENT '查验类型',
  `spec_name` varchar(20) DEFAULT NULL COMMENT '规格',
  `bill_type` int(11) NOT NULL DEFAULT '10' COMMENT '报备类型',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌ID',
  `brand_name` varchar(50) DEFAULT NULL COMMENT '品牌名称',
  `yn` int(11) NOT NULL DEFAULT '1' COMMENT '是否有效',
  `truck_type` int(11) NOT NULL DEFAULT '10' COMMENT '车类型',
  `tally_area_no` varchar(60) DEFAULT NULL COMMENT '摊位号',
  `reason` varchar(100) DEFAULT NULL COMMENT '原因',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `trade_request` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`buyer_id` bigint(20) NOT NULL COMMENT '买家ID',
	`buyer_name` varchar(20) NOT NULL COMMENT '买家姓名',
	`seller_id` bigint(20)  NULL COMMENT '卖家ID',
	`seller_name` varchar(20)  NULL COMMENT '卖家姓名',
	`trade_weight` decimal(10,3) NOT NULL  COMMENT '交易重量',
	`batch_stock_id` bigint(20) NOT NULL COMMENT '商品库存ID',
	`trade_request_type` int(11) NOT NULL  COMMENT '交易类型',
	`trade_request_status` int(11) NOT NULL  COMMENT '交易状态',
	`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
ALTER TABLE event_message MODIFY COLUMN source_business_type TINYINT NOT NULL COMMENT '关联业务类型 用户 10 报备 20 交易 30 ;';
ALTER TABLE event_message MODIFY COLUMN source_business_type tinyint(2) NOT NULL COMMENT '关联业务类型 用户 10 报备 20 交易 30 ;';
ALTER TABLE hztrace.event_message ADD receiver_type TINYINT(2) NOT NULL COMMENT '接收者角色 普通用户10 管理员20';
ALTER TABLE hztrace.event_message MODIFY COLUMN read_flag tinyint(1) DEFAULT 0 NOT NULL COMMENT '已读标志 未读0 已读 1';
ALTER TABLE hztrace.event_message MODIFY COLUMN create_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL;
