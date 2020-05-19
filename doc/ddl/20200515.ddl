

ALTER TABLE `user` ADD COLUMN `qr_status`  int(11)  NULL default 30 COMMENT '二维码状态(默认红色)';
ALTER TABLE `user` ADD COLUMN `user_type`  int(11)  NULL default 10 COMMENT '用户类型';
ALTER TABLE `user` ADD COLUMN `market_id`  bigint(20)  NULL COMMENT '所属市场';

ALTER TABLE `user` ADD COLUMN `license`  varchar(50)  NULL COMMENT '统一信用代码';
ALTER TABLE `user` ADD COLUMN `legal_person`  varchar(50)  NULL COMMENT '法人姓名';
ALTER TABLE `user` ADD COLUMN `manufacturing_license_url`  varchar(50)  NULL COMMENT '生产许可证';
ALTER TABLE `user` ADD COLUMN `operation_license_url`  varchar(50)  NULL COMMENT '经营许可证';


CREATE TABLE `market` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '市场名称',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `upstream` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `upstream_type` int(11) NOT NULL COMMENT '10个人 20.企业',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `telphone` varchar(20) NOT NULL COMMENT '联系方式',
  `name` varchar(20) NOT NULL COMMENT '企业(个人)名称',
  `legal_person` varchar(20) NOT NULL COMMENT '法人姓名',
  `license` varchar(20) NOT NULL COMMENT '统一信用代码',
  `business_license_url` varchar(50)  NULL COMMENT '企业营业执照',
  `manufacturing_license_url`  varchar(50)  NULL COMMENT '生产许可证',
  `operation_license_url`  varchar(50)  NULL COMMENT '经营许可证',

  `card_no_front_url`  varchar(50)  NULL COMMENT '身份证照正面',
  `card_no_back_url`  varchar(50)  NULL COMMENT '身份证照反面URL',

  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `r_user_upstream` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户(商户)ID',
  `upstream_id` bigint(20) NOT NULL COMMENT '上游信息ID',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_qr_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户(商户)ID',
  `qr_item_type` int(11) NOT NULL COMMENT '二维码信息类型',
  `qr_item_status` int(11) NOT NULL COMMENT '二维码信息状态',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_qr_item_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_qr_item_id` bigint(20) NOT NULL COMMENT '用户二维码信息ID',
  `object_id` varchar(15) NOT NULL COMMENT '数据ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `register_bill` ADD COLUMN  `separate_sales_record_id` bigint(20) NULL COMMENT '业户库存信息ID';
ALTER TABLE `register_bill` ADD COLUMN  `upstream_id` bigint(20) NULL COMMENT '上游信息ID';


ALTER TABLE `separate_sales_record` ADD COLUMN  `parent_id` bigint(20) NULL  COMMENT '分销自';
ALTER TABLE `separate_sales_record` ADD COLUMN  `bill_id` bigint(20) NULL  COMMENT '最初登记单ID';
ALTER TABLE `separate_sales_record` ADD COLUMN  `user_product_store_type`  int(11) NOT NULL COMMENT '库存来源类型';
ALTER TABLE `separate_sales_record` ADD COLUMN  `store_weight`  DECIMAL(10,3)  NOT  NULL  default 0 COMMENT '未分销重量';
ALTER TABLE `separate_sales_record` ADD COLUMN  `sales_type`  int(11)    NULL COMMENT '分销类型';
ALTER TABLE `separate_sales_record` ADD COLUMN  `checkin_record_id`  bigint(20)    NULL COMMENT '进门ID';
ALTER TABLE `separate_sales_record` ADD COLUMN  `checkout_record_id`  bigint(20)    NULL COMMENT '出门ID';

/*进出门记录*/
CREATE TABLE `checkin_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `checkin_status` int(11) NOT NULL COMMENT '进门状态',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*进出门记录*/
CREATE TABLE `checkout_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `checkout_status` int(11) NOT NULL COMMENT '出门状态',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;