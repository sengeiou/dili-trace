DROP TABLE IF EXISTS register_head;
CREATE TABLE `register_head` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表主键',
  `code` varchar(20) NOT NULL COMMENT '主台账编号',
  `bill_type` int(11) NOT NULL COMMENT '单据类型。10-正常进场 20-补单 30-外冷分批进场。',
  `user_id` bigint(20) NOT NULL COMMENT '业户ID',
  `name` varchar(50) DEFAULT NULL COMMENT '业户姓名',
  `id_card_no` varchar(20) DEFAULT NULL COMMENT '业户身份证号',
  `third_party_code` varchar(20) DEFAULT NULL COMMENT '经营户卡号',
  `addr` varchar(50) DEFAULT NULL COMMENT '业户地址',
  `phone` varchar(20) DEFAULT NULL COMMENT '业户手机',
  `plate` varchar(15) DEFAULT NULL COMMENT '车牌号',
  `product_id` bigint(20) NOT NULL COMMENT '商品id',
  `product_name` varchar(20) DEFAULT NULL COMMENT '商品名称',
  `measure_type` tinyint(2) NOT NULL COMMENT '计量类型。10-计件 20-计重。默认计件。',
  `piece_num` decimal(10,3) DEFAULT NULL COMMENT '件数',
  `piece_weight` decimal(10,3) DEFAULT NULL COMMENT '件重',
  `weight` decimal(10,3) NOT NULL COMMENT '总重量',
  `remain_weight` decimal(10,3) NOT NULL COMMENT '剩余重量',
  `weight_unit` int(11) NOT NULL COMMENT '重量单位。1-斤 2-公斤。默认1。',
  `upstream_id` bigint(20) NOT NULL COMMENT '上游id',
  `spec_name` varchar(20) DEFAULT NULL COMMENT '规格',
  `origin_id`  bigint(20) DEFAULT NULL COMMENT '产地id',
  `origin_name` varchar(50) DEFAULT NULL COMMENT '产地',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌id',
  `brand_name` varchar(50) DEFAULT NULL COMMENT '品牌名称',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_user` varchar(50) DEFAULT NULL COMMENT '修改人',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(2) DEFAULT NULL COMMENT '是否作废。0-否 1-是',
  `delete_user` varchar(50) DEFAULT NULL COMMENT '作废人',
  `delete_time` datetime DEFAULT NULL COMMENT '作废时间',
  `version` tinyint(4) NOT NULL COMMENT '版本号',
  `reason` varchar(100) DEFAULT NULL COMMENT '原因',
  `active` tinyint(2) DEFAULT NULL COMMENT '是否启用。0-否 1-是',
  `market_id` bigint(20) DEFAULT NULL COMMENT '市场ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*
SQLyog Ultimate
MySQL - 5.7.25 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;
DROP TABLE IF EXISTS countries;
CREATE TABLE `countries` (
    `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
    `code` char(3) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '地区代码',
    `cname` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY `countries_code_index` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

-- 检测主单
DROP TABLE IF EXISTS check_order;
CREATE TABLE `check_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_card` varchar(100) DEFAULT NULL COMMENT '经营户统一社会信用代码/自然人经营户个人身份证号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '检测用户id',
  `user_name` varchar(50) DEFAULT NULL COMMENT '检测用户名称',
  `tally_area_nos` varchar(200) DEFAULT NULL COMMENT '检测用户摊位号',
  `check_no` varchar(20) DEFAULT NULL COMMENT '检测批次号',
  `check_org_code` varchar(50) DEFAULT NULL COMMENT '检测机构编号',
  `check_org_name` varchar(50) DEFAULT NULL COMMENT '检测机构名称',
  `check_result` tinyint(1) DEFAULT NULL COMMENT '检测结果 0合格：1不合格',
  `check_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '检测时间',
  `check_type` tinyint(1) DEFAULT NULL COMMENT '检测类型 1.定性，2.定量',
  `checker_id` bigint(20) DEFAULT NULL COMMENT '检测人id',
  `checker` varchar(100) DEFAULT NULL COMMENT '检测人',
  `goods_name` varchar(20) DEFAULT NULL COMMENT '本地商品名字',
  `goods_code` varchar(50) DEFAULT NULL COMMENT '本地商品编码',
  `market_id` bigint(20) DEFAULT NULL COMMENT '市场id',
  `report_flag` tinyint(1) DEFAULT '-1' COMMENT '上报标志位-1未处理/1待上报/2已上报',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;

-- 检测详情数据
DROP TABLE IF EXISTS check_data;
CREATE TABLE `check_data` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `check_id` BIGINT(20) NOT NULL,
  `project` VARCHAR(100) DEFAULT NULL COMMENT '检测项名称',
  `normal_value` VARCHAR(200) DEFAULT NULL COMMENT '检测标准值',
  `result` VARCHAR(50) DEFAULT NULL COMMENT '检测结果',
  `value` VARCHAR(50) DEFAULT NULL COMMENT '检测数据值',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 杭果交易数据缓存表
DROP TABLE IF EXISTS third_hangguo_trade_data;
CREATE TABLE `third_hangguo_trade_data` (
  `id` BIGINT(11) NOT NULL AUTO_INCREMENT,
  `order_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '交易日期',
  `supplier_no` VARCHAR(50) DEFAULT NULL COMMENT '供应商码',
  `supplier_name` VARCHAR(50) DEFAULT NULL COMMENT '供应商姓名',
  `batch_no` VARCHAR(30) DEFAULT NULL COMMENT '批号',
  `item_number` VARCHAR(20) DEFAULT NULL COMMENT '商品码',
  `item_name` VARCHAR(30) DEFAULT NULL COMMENT '商品名',
  `unit` VARCHAR(50) DEFAULT NULL COMMENT '规格',
  `origin_no` VARCHAR(10) DEFAULT NULL COMMENT '产地编码',
  `origin_name` VARCHAR(50) DEFAULT NULL COMMENT '产地名称',
  `position_no` VARCHAR(10) DEFAULT NULL COMMENT '仓位码',
  `position_name` VARCHAR(50) DEFAULT NULL COMMENT '仓位名称',
  `price` DECIMAL(18,2) DEFAULT NULL COMMENT '成交价格',
  `package_number` INT(11) DEFAULT '0' COMMENT '件数',
  `number` INT(11) DEFAULT '0' COMMENT '成交数量',
  `amount` DECIMAL(18,2) DEFAULT NULL COMMENT '成交金额',
  `weight` DECIMAL(10,3) DEFAULT NULL COMMENT '箱重',
  `trade_no` VARCHAR(50) DEFAULT NULL COMMENT '流水号',
  `pos_no` VARCHAR(50) DEFAULT NULL COMMENT 'POS 机号',
  `pay_way` VARCHAR(10) DEFAULT NULL COMMENT '支付方式',
  `member_no` VARCHAR(50) DEFAULT NULL COMMENT '会员卡号',
  `member_name` VARCHAR(50) DEFAULT NULL COMMENT '会员姓名',
  `total_amount` DECIMAL(18,2) DEFAULT NULL COMMENT '总金额',
  `operator` VARCHAR(50) DEFAULT NULL COMMENT '营业员',
  `payer` VARCHAR(50) DEFAULT NULL COMMENT '收款员',
  `pay_no` VARCHAR(50) DEFAULT NULL COMMENT '支付流水号',
  `register_no` VARCHAR(20) DEFAULT NULL COMMENT '报备单号',
  `handle_flag` TINYINT(2) DEFAULT 1 COMMENT '处理标志位1未处理/2已处理/3无需处理',
  `report_flag` TINYINT(2) DEFAULT 1 COMMENT '上报标志位1未上报/2已上报/3无需上报',
  `handle_remark` VARCHAR(200) DEFAULT NULL COMMENT '处理备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB  DEFAULT CHARSET=utf8mb4;

-- 第三方来源数据缓存表
DROP TABLE IF EXISTS third_party_source_data;
CREATE TABLE `third_party_source_data` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(500) DEFAULT NULL COMMENT '来源名称',
  `type` INT(11) DEFAULT NULL COMMENT '类型',
  `operator_id` BIGINT(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` VARCHAR(20) DEFAULT NULL COMMENT '操作人ID',
  `data` longtext COMMENT '数据详情',
  `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `report_flag` TINYINT(1) DEFAULT '-1' COMMENT '上报标志位-1未处理/1待上报/2已上报',
  `market_id` BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 处置单
DROP TABLE IF EXISTS check_order_dispose;
CREATE TABLE `check_order_dispose` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `check_no` varchar(20) DEFAULT NULL COMMENT '检测批次号',
  `market_id` bigint(20) DEFAULT NULL COMMENT '市场id',
  `dispose_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '处置日期',
  `dispose_desc` varchar(200) DEFAULT NULL COMMENT '处置备注',
  `dispose_num` int(11) DEFAULT '0' COMMENT '处置数量',
  `dispose_result` varchar(3) DEFAULT NULL COMMENT '处置结果',
  `dispose_type` varchar(3) DEFAULT NULL COMMENT '处置类型',
  `disposer_id` bigint(20) DEFAULT NULL COMMENT '处置人id',
  `disposer` varchar(50) DEFAULT NULL COMMENT '处置人',
  `report_flag` tinyint(1) DEFAULT '-1' COMMENT '上报标志位-1未处理/1待上报/2已上报',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 对业务表增加市场ID
ALTER TABLE brand ADD COLUMN market_id BIGINT(20) DEFAULT 1 COMMENT '市场ID';
ALTER TABLE category ADD COLUMN market_id BIGINT(20) DEFAULT 1 COMMENT '市场ID';
ALTER TABLE checkinout_record ADD COLUMN market_id BIGINT(20) DEFAULT 1 COMMENT '市场ID';
ALTER TABLE product_stock ADD COLUMN market_id BIGINT(20) DEFAULT 1 COMMENT '市场ID';
ALTER TABLE register_bill ADD COLUMN market_id BIGINT(20) DEFAULT 1 COMMENT '市场ID';
ALTER TABLE sys_config ADD COLUMN market_id BIGINT(20) DEFAULT 1 COMMENT '市场ID';
ALTER TABLE tally_area_no ADD COLUMN market_id BIGINT(20) DEFAULT 1 COMMENT '市场ID';
ALTER TABLE third_party_push_data ADD COLUMN market_id BIGINT(20) DEFAULT 1 COMMENT '市场ID';
ALTER TABLE third_party_report_data ADD COLUMN market_id BIGINT(20) DEFAULT 1 COMMENT '市场ID';
ALTER TABLE trade_order ADD COLUMN buyer_market_id BIGINT(20) DEFAULT 1 COMMENT '买家市场ID';
ALTER TABLE trade_order ADD COLUMN seller_market_id BIGINT(20) DEFAULT 1 COMMENT '卖家市场ID';
ALTER TABLE trade_request ADD COLUMN buyer_market_id BIGINT(20) DEFAULT 1 COMMENT '买家市场ID';
ALTER TABLE trade_request ADD COLUMN seller_market_id BIGINT(20) DEFAULT 1 COMMENT '卖家市场ID';
ALTER TABLE trade_push_log CHANGE modified modified TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP  NOT NULL  COMMENT '修改时间';
ALTER TABLE trade_push_log ADD COLUMN market_id BIGINT(20) DEFAULT 1 COMMENT '市场ID';
ALTER TABLE upstream ADD COLUMN market_id BIGINT(20) DEFAULT 1 COMMENT '市场ID';
ALTER TABLE `user` MODIFY market_id BIGINT(20) DEFAULT 1 COMMENT '市场ID';

-- 市场表新增对接信息
ALTER TABLE market ADD COLUMN app_id BIGINT(20) COMMENT '第三方对接平台应用ID';
ALTER TABLE market ADD COLUMN app_secret VARCHAR(50) COMMENT '第三方对接平台应用秘钥';
ALTER TABLE market ADD COLUMN context_url VARCHAR(50) COMMENT '第三方对接平台url地址';
ALTER TABLE market ADD COLUMN platform_market_id BIGINT(20) COMMENT '第三方对接平台市场ID';

-- 报备单新增主台帐类型字段
ALTER TABLE register_bill ADD COLUMN register_head_code VARCHAR(20) COMMENT '主台账编号';
ALTER TABLE register_bill ADD COLUMN third_party_code VARCHAR(20) COMMENT '经营户卡号';
ALTER TABLE register_bill ADD COLUMN area VARCHAR(20) COMMENT '区号';
ALTER TABLE register_bill ADD COLUMN measure_type tinyint(2) DEFAULT 20 COMMENT '计量类型。10-计件 20-计重。默认计件。';
ALTER TABLE register_bill ADD COLUMN piece_num decimal(10,3) COMMENT '件数';
ALTER TABLE register_bill ADD COLUMN piece_weight decimal(10,3) COMMENT '件重';
ALTER TABLE register_bill ADD COLUMN remark VARCHAR(200) COMMENT '备注';
ALTER TABLE register_bill ADD COLUMN create_user VARCHAR(50) COMMENT '创建人';
ALTER TABLE register_bill ADD COLUMN delete_user VARCHAR(50) COMMENT '作废人';
ALTER TABLE register_bill ADD COLUMN delete_time datetime DEFAULT  NULL COMMENT '作废时间';
ALTER TABLE register_bill ADD COLUMN packaging VARCHAR(20) COMMENT '包装';
ALTER TABLE register_bill ADD COLUMN order_type tinyint(2) DEFAULT 1 COMMENT '订单类型 1.报备单 2.进门登记单';

-- 图片新增类型区分报备单、检测单、处置单、主台账单
ALTER TABLE image_cert ADD COLUMN bill_type TINYINT(2) DEFAULT 1 COMMENT '单据类型。1-报备单 2-检测单 3-检测不合格处置单 4-进门主台账单。默认为1';;

-- 商品新增第三方编码、是否显示字段
ALTER TABLE category  ADD COLUMN code VARCHAR(20) NULL  COMMENT '第三方编码' ;
ALTER TABLE category  ADD COLUMN is_show TINYINT(1) DEFAULT 1  COMMENT '登记显示,1显示/2不显示'  ;
ALTER TABLE category  ADD COLUMN type TINYINT(1) DEFAULT 1  COMMENT '商品类型;1杭水/2检测商品'  ;
ALTER TABLE category  ADD COLUMN specification VARCHAR(20) NULL  COMMENT '商品规格名'  ;
ALTER TABLE category  ADD COLUMN parent_code VARCHAR(20) NULL  COMMENT '父级第三方编码'  ;

-- 用户表新增显示字段
ALTER TABLE `user`  ADD COLUMN `third_party_code` VARCHAR(50) NULL  COMMENT '第三方编码';
ALTER TABLE `user`  ADD COLUMN `credential_type` VARCHAR(10) NULL  COMMENT '证件类型';
ALTER TABLE `user`  ADD COLUMN `credential_name` VARCHAR(20) NULL  COMMENT '证件名称';
ALTER TABLE `user`  ADD COLUMN `credential_number` VARCHAR(30) NULL  COMMENT '证件号码';
ALTER TABLE `user`  ADD COLUMN `credential_url` VARCHAR(100) NULL  COMMENT '证件图片地址';
ALTER TABLE `user`  ADD COLUMN `id_addr` VARCHAR(50) NULL  COMMENT '身份证地址';
ALTER TABLE `user`  ADD COLUMN `whereis` VARCHAR(50) NULL  COMMENT '商品去向';
ALTER TABLE `user`  ADD COLUMN `credit_limit` VARCHAR(11) NULL  COMMENT '授信额度';
ALTER TABLE `user`  ADD COLUMN `effective_date` TIMESTAMP NULL  COMMENT '卡有效期';
ALTER TABLE `user`  ADD COLUMN `remark` VARCHAR(100) NULL  COMMENT '备注';
ALTER TABLE `user`  ADD COLUMN `sex` VARCHAR(2) NULL  COMMENT '性别';
ALTER TABLE `user`  ADD COLUMN `fixed_telephone` VARCHAR(20) NULL  COMMENT '固定电话';
ALTER TABLE `user`  ADD COLUMN `charge_rate` DECIMAL(5,2) DEFAULT '0.00'  COMMENT '手续费折扣率';
ALTER TABLE `user`  ADD COLUMN `manger_rate` DECIMAL(5,2) DEFAULT '0.00'  COMMENT '包装管理费折扣率';
ALTER TABLE `user`  ADD COLUMN `storage_rate` DECIMAL(5,2) DEFAULT '0.00'  COMMENT '仓储费折扣率';
ALTER TABLE `user`  ADD COLUMN `assess_rate` DECIMAL(5,2) DEFAULT '0.00'  COMMENT '员工考核折扣率';
ALTER TABLE `user`  ADD COLUMN `approver` VARCHAR(10) NULL  COMMENT '折扣率批准人';
ALTER TABLE `user`  ADD COLUMN `supplier_type` VARCHAR(10) NULL  COMMENT '供应商类型（大客户、临时客户）';

-- 交易表新增杭果显示字段
ALTER TABLE `trade_request` ADD COLUMN `source_type` TINYINT(1) DEFAULT 1 COMMENT '来源类型 1农溯安/2杭果交易数据';
ALTER TABLE `trade_request` ADD COLUMN `report_flag` TINYINT(1) DEFAULT 1 COMMENT '是否上报 1上报/2不上报';
ALTER TABLE `trade_request` ADD COLUMN `batch_no` VARCHAR(50) NULL COMMENT '批号'; 
ALTER TABLE `trade_request` ADD COLUMN `origin_name` VARCHAR(50) NULL COMMENT '产地名称'; 
ALTER TABLE `trade_request` ADD COLUMN `position_no` VARCHAR(50) NULL COMMENT '仓位码'; 
ALTER TABLE `trade_request` ADD COLUMN `position_name` VARCHAR(50) NULL COMMENT '仓位名称'; 
ALTER TABLE `trade_request` ADD COLUMN `price` DECIMAL(10,3) DEFAULT '0.000' COMMENT '成交价格'; 
ALTER TABLE `trade_request` ADD COLUMN `package_number` INT(11) DEFAULT '0'  COMMENT '件数'; 
ALTER TABLE `trade_request` ADD COLUMN `number` INT(11)  DEFAULT '0'  COMMENT '成交数量'; 
ALTER TABLE `trade_request` ADD COLUMN `amount` DECIMAL(10,3) DEFAULT '0.000' COMMENT '成交金额'; 
ALTER TABLE `trade_request` ADD COLUMN `pos_no` VARCHAR(50) NULL COMMENT 'POS机号'; 
ALTER TABLE `trade_request` ADD COLUMN `pay_way` VARCHAR(50) NULL COMMENT '支付方式'; 
ALTER TABLE `trade_request` ADD COLUMN `total_amount` DECIMAL(10,3) DEFAULT '0.000' COMMENT '总金额'; 
ALTER TABLE `trade_request` ADD COLUMN `operator` VARCHAR(50) NULL COMMENT '营业员'; 
ALTER TABLE `trade_request` ADD COLUMN `payer` VARCHAR(50) NULL COMMENT '收款员'; 
ALTER TABLE `trade_request` ADD COLUMN `pay_no` VARCHAR(50) NULL COMMENT '支付流水号'; 
ALTER TABLE `trade_request` ADD COLUMN `third_trade_no` VARCHAR(50) NULL COMMENT '第三方交易流水号'; 



-- 将当前数据都归纳给杭水
UPDATE brand SET market_id = 1;
UPDATE category SET market_id = 1;
UPDATE checkinout_record SET market_id = 1;
UPDATE product_stock SET market_id = 1;
UPDATE register_bill SET market_id = 1;
UPDATE sys_config SET market_id = 1;
UPDATE tally_area_no SET market_id = 1;
UPDATE third_party_push_data SET market_id = 1;
UPDATE third_party_report_data SET market_id = 1;
UPDATE trade_order SET buyer_market_id = 1;
UPDATE trade_order SET seller_market_id = 1;
UPDATE trade_request SET buyer_market_id = 1;
UPDATE trade_request SET seller_market_id = 1;
UPDATE trade_push_log SET market_id = 1;
UPDATE upstream SET market_id = 1;

-- 新增市场初始化数据***!注意context_url字段!****
insert into `market` (`id`, `name`, `operator_id`, `operator_name`, `created`, `modified`, `app_id`, `app_secret`, `context_url`, `platform_market_id`)
 values('1','杭州水产市场','260','超级用户',now(),now(),'61835297','baa05febaa330aa9ea5ccaa07ed140b24e82387f','https://pubapi.rongshian.com','330110800');
insert into `market` (`id`, `name`, `operator_id`, `operator_name`, `created`, `modified`, `app_id`, `app_secret`, `context_url`, `platform_market_id`)
 values('2','杭州果品市场','260','超级用户',now(),now(),'758639891939590144','ac7fd1182026dc0b3ca35b8d168c4f6f187d8e5d','https://pubapi.rongshian.com','33040031');

-- 对register_head增加订单编号
insert into `biz_number` ( `type`, `value`, `memo`, `version`, `modified`, `created`) values
('register_head',concat(date_format(now(),'%Y%m%d'),'00000'),'主台账编码','0',now(),now());


-- 国外产地默认初始化数据
insert into `countries` (`code`, `cname`) values('ARG','阿根廷');
insert into `countries` (`code`, `cname`) values('AUS','澳大利亚');
insert into `countries` (`code`, `cname`) values('BRA','巴西');
insert into `countries` (`code`, `cname`) values('CHL','智利');
insert into `countries` (`code`, `cname`) values('ECU','厄瓜多尔');
insert into `countries` (`code`, `cname`) values('FRA','法国');
insert into `countries` (`code`, `cname`) values('ISR','以色列');
insert into `countries` (`code`, `cname`) values('ITA','意大利');
insert into `countries` (`code`, `cname`) values('NZL','新西兰');
insert into `countries` (`code`, `cname`) values('PHL','菲律宾');
insert into `countries` (`code`, `cname`) values('ZAF','南非');
insert into `countries` (`code`, `cname`) values('THA','泰国');
insert into `countries` (`code`, `cname`) values('USA','美国');
insert into `countries` (`code`, `cname`) values('VNM','越南');
insert into `countries` (`code`, `cname`) values('JPN','日本');
insert into `countries` (`code`, `cname`) values('MMR','缅甸');

-- 增加杭果的区位号
insert into `tally_area_no` (`number`, `street`, `area`, `created`, `modified`, `market_id`) values('',NULL,'A区',now(),now(),2);
insert into `tally_area_no` (`number`, `street`, `area`, `created`, `modified`, `market_id`) values('',NULL,'B区',now(),now(),2);
insert into `tally_area_no` (`number`, `street`, `area`, `created`, `modified`, `market_id`) values('',NULL,'C区',now(),now(),2);
insert into `tally_area_no` (`number`, `street`, `area`, `created`, `modified`, `market_id`) values('',NULL,'D区',now(),now(),2);
insert into `tally_area_no` (`number`, `street`, `area`, `created`, `modified`, `market_id`) values('',NULL,'E区',now(),now(),2);
insert into `tally_area_no` (`number`, `street`, `area`, `created`, `modified`, `market_id`) values('',NULL,'F区',now(),now(),2);