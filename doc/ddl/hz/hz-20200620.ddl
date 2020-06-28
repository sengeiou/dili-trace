
CREATE TABLE `category` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`parent_id` bigint(20)  NULL COMMENT '上一级ID',
	`name` varchar(20)  NULL COMMENT '名称',
	`full_name` varchar(20)  NULL COMMENT '全名',
	`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `image_cert` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`target_id` bigint(20)  NULL COMMENT '所属数据ID',
	`url` varchar(200)  NULL COMMENT '图片URL',
	`cert_type` int(11) not NULL COMMENT '图片类型',
	`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `verify_history` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`bill_id` bigint(20)  NULL COMMENT '所属数据ID',
	`from_verify_status` int(11) NOT NULL COMMENT '初始审核状态',
	`to_verify_status` int(11)  NOT NULL COMMENT '审核状态',
	`verify_user_id` bigint(20) NOT NULL COMMENT '审核人ID',
	`verify_user_name` varchar(50) NOT NULL COMMENT '审核人姓名',
	`valid` int(11) NOT NULL COMMENT '是否是当前有效的数据',
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
	`stock_weight` decimal(10,3) NOT NULL default 0 COMMENT '库存重量',
	`total_weight` decimal(10,3) NOT NULL default 0 COMMENT '总重量',
	`weight_unit` int(11) NOT NULL default 10 COMMENT '重量单位',
	`status` int(11) NOT NULL default 0 COMMENT '交易单状态',
	`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO category (parent_id,name,full_name,created,modified) VALUES 
(NULL,'贝类','贝类',now(),now())
,(NULL,'淡水鱼','淡水鱼',now(),now())
,(NULL,'高档海鲜','高档海鲜',now(),now())
,(NULL,'海水鱼','海水鱼',now(),now())
,(NULL,'河鲜','河鲜',now(),now())
,(NULL,'虾类','虾类',now(),now())
,(NULL,'其他','其他',now(),now())
;
INSERT INTO category (parent_id,name,full_name,created,modified) VALUES 
(1,'白蛤','贝类,白蛤',now(),now())
,(1,'白玉贝','贝类,白玉贝',now(),now())
,(1,'北极贝','贝类,北极贝',now(),now())
,(1,'朝鲜蚌','贝类,朝鲜蚌',now(),now())
,(1,'蛏子','贝类,蛏子',now(),now())
,(1,'赤贝','贝类,赤贝',now(),now())
,(1,'大角螺','贝类,大角螺',now(),now())
,(1,'淡菜','贝类,淡菜',now(),now())
,(1,'刀贝','贝类,刀贝',now(),now())
,(1,'钉螺','贝类,钉螺',now(),now())
,(1,'肥贝','贝类,肥贝',now(),now())
,(1,'广州蚌仔','贝类,广州蚌仔',now(),now())
,(1,'蛤蜊','贝类,蛤蜊',now(),now())
,(1,'海蚌','贝类,海蚌',now(),now())
,(1,'海带花','贝类,海带花',now(),now())
,(1,'海瓜子','贝类,海瓜子',now(),now())
,(1,'海花','贝类,海花',now(),now())
,(1,'海螺','贝类,海螺',now(),now())
,(1,'河蚌','贝类,河蚌',now(),now())
,(1,'红心蛏','贝类,红心蛏',now(),now())
,(1,'花蛤','贝类,花蛤',now(),now())
,(1,'花螺','贝类,花螺',now(),now())
,(1,'黄贝','贝类,黄贝',now(),now())
,(1,'黄金贝','贝类,黄金贝',now(),now())
,(1,'黄金螺','贝类,黄金螺',now(),now())
,(1,'黄螺','贝类,黄螺',now(),now())
,(1,'扣贝','贝类,扣贝',now(),now())
,(1,'辣螺','贝类,辣螺',now(),now())
,(1,'老头蛤','贝类,老头蛤',now(),now())
,(1,'龙须草','贝类,龙须草',now(),now())
,(1,'螺蛳','贝类,螺蛳',now(),now())
,(1,'芒果贝','贝类,芒果贝',now(),now())
,(1,'毛贝','贝类,毛贝',now(),now())
,(1,'美国贝','贝类,美国贝',now(),now())
,(1,'牡蛎','贝类,牡蛎',now(),now())
,(1,'泥螺','贝类,泥螺',now(),now())
,(1,'鸟贝','贝类,鸟贝',now(),now())
,(1,'七彩贝','贝类,七彩贝',now(),now())
,(1,'青蛤','贝类,青蛤',now(),now())
,(1,'青口贝','贝类,青口贝',now(),now())
,(1,'扇贝','贝类,扇贝',now(),now())
,(1,'生蚝','贝类,生蚝',now(),now())
,(1,'湿海蛎','贝类,湿海蛎',now(),now())
,(1,'石头贝','贝类,石头贝',now(),now())
,(1,'石蟹','贝类,石蟹',now(),now())
,(1,'特大贝','贝类,特大贝',now(),now())
,(1,'天鹅蛋','贝类,天鹅蛋',now(),now())
,(1,'田螺','贝类,田螺',now(),now())
,(1,'文蛤','贝类,文蛤',now(),now())
,(1,'蜗牛','贝类,蜗牛',now(),now())
,(1,'西施贝','贝类,西施贝',now(),now())
,(1,'香螺','贝类,香螺',now(),now())
,(1,'小蚌','贝类,小蚌',now(),now())
,(1,'血蛤','贝类,血蛤',now(),now())
,(1,'一点红','贝类,一点红',now(),now())
,(1,'油蛤','贝类,油蛤',now(),now())
,(1,'月亮贝','贝类,月亮贝',now(),now())
,(1,'珍珠贝','贝类,珍珠贝',now(),now())
,(1,'芝麻螺','贝类,芝麻螺',now(),now())
,(1,'竹蛏','贝类,竹蛏',now(),now())
,(1,'象拔蚌','贝类,象拔蚌',now(),now())
,(1,'鲍鱼','贝类,鲍鱼',now(),now())
;
INSERT INTO category (parent_id,name,full_name,created,modified) VALUES 
(2,'草鱼','淡水鱼,草鱼',now(),now())
,(2,'花鲢','淡水鱼,花鲢',now(),now())
,(2,'白莲','淡水鱼,白莲',now(),now())
,(2,'鲫鱼','淡水鱼,鲫鱼',now(),now())
,(2,'鳊鱼','淡水鱼,鳊鱼',now(),now())
,(2,'黑鱼','淡水鱼,黑鱼',now(),now())
,(2,'鲤鱼','淡水鱼,鲤鱼',now(),now())
,(2,'青鱼','淡水鱼,青鱼',now(),now())
,(2,'湘鲫','淡水鱼,湘鲫',now(),now())
,(2,'鲈鱼','淡水鱼,鲈鱼',now(),now())
,(2,'桂鱼','淡水鱼,桂鱼',now(),now())
,(2,'江刺鱼','淡水鱼,江刺鱼',now(),now())
,(2,'白条','淡水鱼,白条',now(),now())
,(2,'罗非鱼','淡水鱼,罗非鱼',now(),now())
,(2,'河鳗','淡水鱼,河鳗',now(),now())
,(2,'江鳗','淡水鱼,江鳗',now(),now())
,(2,'鲶鱼','淡水鱼,鲶鱼',now(),now())
,(2,'红鮰','淡水鱼,红鮰',now(),now())
,(2,'黑鮰','淡水鱼,黑鮰',now(),now())
,(2,'洋花鱼','淡水鱼,洋花鱼',now(),now())
,(2,'鲻鱼','淡水鱼,鲻鱼',now(),now())
,(2,'太阳鱼','淡水鱼,太阳鱼',now(),now())
,(2,'鳕鱼','淡水鱼,鳕鱼',now(),now())
,(2,'牛尾鱼','淡水鱼,牛尾鱼',now(),now())
,(2,'中华鲟','淡水鱼,中华鲟',now(),now())
,(2,'鱼钩鱼','淡水鱼,鱼钩鱼',now(),now())
,(2,'鸭嘴鱼','淡水鱼,鸭嘴鱼',now(),now())
;
INSERT INTO category (parent_id,name,full_name,created,modified) VALUES
,(3,'生态黄鱼','高档海鲜,生态黄鱼',now(),now())
,(3,'十八梅','高档海鲜,十八梅',now(),now())
,(3,'美国红鱼','高档海鲜,美国红鱼',now(),now())
,(3,'金鲳鱼','高档海鲜,金鲳鱼',now(),now())
,(3,'黑珍鲷','高档海鲜,黑珍鲷',now(),now())
,(3,'青斑','高档海鲜,青斑',now(),now())
,(3,'石斑','高档海鲜,石斑',now(),now())
,(3,'龙纹斑','高档海鲜,龙纹斑',now(),now())
,(3,'老虎斑','高档海鲜,老虎斑',now(),now())
,(3,'中华鲟','高档海鲜,中华鲟',now(),now())
,(3,'淡水小石斑','高档海鲜,淡水小石斑',now(),now())
,(3,'笋壳鱼','高档海鲜,笋壳鱼',now(),now())
,(3,'东星斑','高档海鲜,东星斑',now(),now())
,(3,'多宝鱼','高档海鲜,多宝鱼',now(),now())
,(3,'左口鱼','高档海鲜,左口鱼',now(),now())
,(3,'鲍鱼','高档海鲜,鲍鱼',now(),now())
,(3,'澳鲍','高档海鲜,澳鲍',now(),now())
,(3,'雪鱼','高档海鲜,雪鱼',now(),now())
,(3,'三文鱼','高档海鲜,三文鱼',now(),now())
,(3,'金枪鱼','高档海鲜,金枪鱼',now(),now())
,(3,'海鲈鱼','高档海鲜,海鲈鱼',now(),now())
,(3,'黄杉鱼','高档海鲜,黄杉鱼',now(),now())
,(3,'柳根鱼','高档海鲜,柳根鱼',now(),now())
,(3,'香鱼','高档海鲜,香鱼',now(),now())
,(3,'老鼠斑','高档海鲜,老鼠斑',now(),now())
,(3,'河豚','高档海鲜,河豚',now(),now())
,(3,'澳龙','高档海鲜,澳龙',now(),now())
,(3,'红龙','高档海鲜,红龙',now(),now())
,(3,'波士顿龙','高档海鲜,波士顿龙',now(),now())
,(3,'小青龙','高档海鲜,小青龙',now(),now())
,(3,'火龙','高档海鲜,火龙',now(),now())
,(3,'南非龙','高档海鲜,南非龙',now(),now())
,(3,'象拔蚌','高档海鲜,象拔蚌',now(),now())
,(3,'海参','高档海鲜,海参',now(),now())
,(3,'珍宝蟹','高档海鲜,珍宝蟹',now(),now())
,(3,'面包蟹','高档海鲜,面包蟹',now(),now())
,(3,'帝王蟹','高档海鲜,帝王蟹',now(),now())
,(3,'皇帝蟹','高档海鲜,皇帝蟹',now(),now())
,(3,'雪蟹','高档海鲜,雪蟹',now(),now())
,(3,'长脚蟹','高档海鲜,长脚蟹',now(),now())
,(3,'红花蟹','高档海鲜,红花蟹',now(),now())
,(3,'兰花蟹','高档海鲜,兰花蟹',now(),now())
,(3,'梭子蟹','高档海鲜,梭子蟹',now(),now())
,(3,'石蟹','高档海鲜,石蟹',now(),now())
,(3,'沙蟹','高档海鲜,沙蟹',now(),now())
,(3,'蓝蟹','高档海鲜,蓝蟹',now(),now())
,(3,'红毛蟹','高档海鲜,红毛蟹',now(),now())
,(3,'青蟹','高档海鲜,青蟹',now(),now())
,(3,'椰子蟹','高档海鲜,椰子蟹',now(),now())
,(3,'老虎蟹','高档海鲜,老虎蟹',now(),now())
;
INSERT INTO category (parent_id,name,full_name,created,modified) VALUES 
(4,'米鱼','海水鱼,米鱼',now(),now())
,(4,'龙头鱼','海水鱼,龙头鱼',now(),now())
,(4,'马面鱼','海水鱼,马面鱼',now(),now())
,(4,'龙利鱼','海水鱼,龙利鱼',now(),now())
,(4,'月亮鱼','海水鱼,月亮鱼',now(),now())
,(4,'刀鱼','海水鱼,刀鱼',now(),now())
,(4,'鲅鱼','海水鱼,鲅鱼',now(),now())
,(4,'青占鱼','海水鱼,青占鱼',now(),now())
,(4,'安康鱼','海水鱼,安康鱼',now(),now())
,(4,'墨鱼','海水鱼,墨鱼',now(),now())
,(4,'鱿鱼','海水鱼,鱿鱼',now(),now())
,(4,'章鱼','海水鱼,章鱼',now(),now())
,(4,'马头鱼','海水鱼,马头鱼',now(),now())
,(4,'海鳗','海水鱼,海鳗',now(),now())
,(4,'海虾','海水鱼,海虾',now(),now())
,(4,'带鱼','海水鱼,带鱼',now(),now())
,(4,'鲳鱼','海水鱼,鲳鱼',now(),now())
,(4,'鳓鱼','海水鱼,鳓鱼',now(),now())
,(4,'鲥鱼','海水鱼,鲥鱼',now(),now())
,(4,'黄花鱼','海水鱼,黄花鱼',now(),now())
,(4,'黄鲷鱼','海水鱼,黄鲷鱼',now(),now())
,(4,'黄菇鱼','海水鱼,黄菇鱼',now(),now())
,(4,'银鳕鱼','海水鱼,银鳕鱼',now(),now())
,(4,'鲽鱼','海水鱼,鲽鱼',now(),now())
,(4,'石斑鱼','海水鱼,石斑鱼',now(),now())
,(4,'金枪鱼','海水鱼,金枪鱼',now(),now())
,(4,'海鲈鱼','海水鱼,海鲈鱼',now(),now())
,(4,'秋刀鱼','海水鱼,秋刀鱼',now(),now())
,(4,'长寿鱼','海水鱼,长寿鱼',now(),now())
,(4,'虎头鱼','海水鱼,虎头鱼',now(),now())
,(4,'白菇鱼','海水鱼,白菇鱼',now(),now())
,(4,'梅童鱼','海水鱼,梅童鱼',now(),now())
,(4,'八爪鱼','海水鱼,八爪鱼',now(),now())
;
INSERT INTO category (parent_id,name,full_name,created,modified) VALUES 
(5,'黄鳝','河鲜,黄鳝',now(),now())
,(5,'小龙虾','河鲜,小龙虾',now(),now())
,(5,'甲鱼','河鲜,甲鱼',now(),now())
,(5,'泥鳅','河鲜,泥鳅',now(),now())
,(5,'牛蛙','河鲜,牛蛙',now(),now())
,(5,'江鳗','河鲜,江鳗',now(),now())
,(5,'河鳗','河鲜,河鳗',now(),now())
,(5,'白鳗','河鲜,白鳗',now(),now())
,(5,'巴西龟','河鲜,巴西龟',now(),now())
,(5,'草龟','河鲜,草龟',now(),now())
,(5,'鳄鱼龟','河鲜,鳄鱼龟',now(),now())
,(5,'湖蟹','河鲜,湖蟹',now(),now())
;
INSERT INTO category (parent_id,name,full_name,created,modified) VALUES 
(6,'黑虎虾','虾类,黑虎虾',now(),now())
,(6,'白虾','虾类,白虾',now(),now())
,(6,'南美白对虾','虾类,南美白对虾',now(),now())
,(6,'基围虾','虾类,基围虾',now(),now())
,(6,'河虾','虾类,河虾',now(),now())
,(6,'沼虾','虾类,沼虾',now(),now())
,(6,'竹节虾','虾类,竹节虾',now(),now())
,(6,'富贵虾','虾类,富贵虾',now(),now())
,(6,'牡丹虾','虾类,牡丹虾',now(),now())
,(6,'青虾','虾类,青虾',now(),now())
,(6,'明虾','虾类,明虾',now(),now())
,(6,'草虾','虾类,草虾',now(),now());
INSERT INTO category (parent_id,name,full_name,created,modified) VALUES 
(7,'鱼肚','其他,鱼肚',now(),now())
,(7,'八爪鱼','其他,八爪鱼',now(),now())
,(7,'鱿鱼','其他,鱿鱼',now(),now())
,(7,'蚕蛹','其他,蚕蛹',now(),now())
,(7,'叽咕','其他,叽咕',now(),now())
,(7,'海参','其他,海参',now(),now())
;


drop table `quality_trace_trade_bill`;
drop table `quality_trace_trade_bill_syncpoint`;
drop table `user_tally_area`;

drop table `check_sheet`;
drop table `check_sheet_detail`;
ALTER TABLE `register_bill` ADD COLUMN  `weight_unit` int(11) NOT NULL default 10 COMMENT '重量单位';
ALTER TABLE `register_bill` ADD COLUMN  `verify_status` int(11) not null default 0 COMMENT '查验状态';
ALTER TABLE `register_bill` ADD COLUMN  `preserve_type` int(11) NOT NULL  default 10 COMMENT '保存类型';
ALTER TABLE `register_bill` MODIFY COLUMN weight decimal(10,3)  NOT NULL default 0 COMMENT '重量';


ALTER TABLE `register_bill` DROP COLUMN `sales_type`;

ALTER TABLE `register_bill` DROP COLUMN `register_source`;
ALTER TABLE `register_bill` DROP COLUMN `tally_area_no`;

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

