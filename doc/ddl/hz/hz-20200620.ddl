
CREATE TABLE `image_cert` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT,
	`target_id` bigint(20)  NULL COMMENT '所属数据ID',
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
	`trade_weight` decimal(10,3) NOT NULL default 0 COMMENT '交易重量',
	`inventory_weight` decimal(10,3) NOT NULL default 0 COMMENT '库存重量',
	`total_weight` decimal(10,3) NOT NULL default 0 COMMENT '总重量',
	`weight_unit` int(11) NOT NULL default 10 COMMENT '重量单位',
	`created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


ALTER TABLE `register_bill` ADD COLUMN  `verify_status` int(11) not null default 0 COMMENT '查验状态';
ALTER TABLE `register_bill` DROP COLUMN `sales_type`;



ALTER TABLE `checkinout_record` ADD COLUMN  `inout_weight` decimal(10,3) not null default 0 COMMENT '进出门重量';
ALTER TABLE `checkinout_record` ADD COLUMN  `trade_detail_id` bigint not null default 0 COMMENT '分销ID';

ALTER TABLE `checkinout_record` DROP COLUMN `sales_weight`;
ALTER TABLE `checkinout_record` DROP COLUMN `seperate_sales_id`;

