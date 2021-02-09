

DROP TABLE IF EXISTS category;
CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上一级ID',
  `category_id` bigint(20) DEFAULT NULL COMMENT 'categoryID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '名称',
  `full_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL  COMMENT '全名',
  `level` int(11) NOT NULL DEFAULT '1' COMMENT '层级',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `market_id` varchar(20) DEFAULT NULL,
  `code` varchar(20) DEFAULT NULL COMMENT '第三方编码',
  `is_show` varchar(1) DEFAULT '1' COMMENT '登记显示,1显示,2不显示',
  `type` tinyint(1) DEFAULT '1' COMMENT '商品类型,1杭水，2检测商品',
  `specification` varchar(20) DEFAULT NULL COMMENT '商品规格名',
  `parent_code` varchar(20) DEFAULT NULL COMMENT '父级第三方编码',
  `uap_id` bigint(20) DEFAULT NULL COMMENT '调用uap获取商品id',
  `uap_parent_id` bigint(20) DEFAULT NULL COMMENT '调用uap获取parentid(处理后)',
  `old_uap_parent_id` bigint(20) DEFAULT NULL COMMENT '调用uap获取商品parentid',
  `last_sync_time` DATETIME NULL COMMENT '最后的同步时间',
  `last_sync_success` INT NULL COMMENT '最后是否同步成功(1:成功,0:失败)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

ALTER TABLE dili_trace.category ADD CONSTRAINT category_market_unique UNIQUE KEY (category_id,market_id);

ALTER TABLE dili_trace.`user` ADD qr_content varchar(500) NULL COMMENT '最后的二维码变更内容';
ALTER TABLE dili_trace.`user` ADD qr_history_id bigint(20) NULL COMMENT '最后的二维码变更历史ID';

ALTER TABLE dili_trace.`user_qr_history` ADD qr_history_event_id bigint(20) NULL COMMENT 'qr事件ID';
ALTER TABLE dili_trace.`user_qr_history` ADD qr_history_event_type INT NULL COMMENT 'qr事件类型';
ALTER TABLE dili_trace.`user_qr_history` ADD user_info_id bigint(20) NULL COMMENT '市场ID';

ALTER TABLE dili_trace.`user` DROP COLUMN `password`;
ALTER TABLE dili_trace.`user` DROP COLUMN `version`;


ALTER TABLE dili_trace.user_qr_history DROP COLUMN user_id;
ALTER TABLE dili_trace.user_qr_history DROP COLUMN user_name;
ALTER TABLE dili_trace.user_qr_history DROP COLUMN bill_id;
ALTER TABLE dili_trace.user_qr_history DROP COLUMN trade_request_id;

ALTER TABLE dili_trace.quality_trace_trade_bill_syncpoint DROP COLUMN order_version;
ALTER TABLE dili_trace.quality_trace_trade_bill DROP COLUMN order_version;

ALTER TABLE dili_trace.`register_bill` DROP COLUMN `version`;



ALTER TABLE dili_trace.`user_history` DROP COLUMN `password`;
ALTER TABLE dili_trace.`user_history` DROP COLUMN `version`;
ALTER TABLE dili_trace.`user_history` DROP COLUMN `card_no_front_url`;
ALTER TABLE dili_trace.`user_history` DROP COLUMN `card_no_back_url`;
ALTER TABLE dili_trace.`user_history` DROP COLUMN `business_license_url`;
/*====================*/

CREATE TABLE `default_field_detail` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `module_type` int NOT NULL COMMENT '模块',
    `field_id` varchar(50)  NULL COMMENT '字段ID',
    `field_label` varchar(50) NOT  NULL COMMENT '字段Label',
    `field_name` varchar(50) NOT  NULL COMMENT '字段名称',
    `is_display` int NOT NULL DEFAULT 0 COMMENT '是否显示',
    `is_required` int NOT NULL DEFAULT 0 COMMENT '是否必填',
    `default_value` varchar(50)  NULL  COMMENT '默认值',
    `x_path` varchar(50) NOT NULL  COMMENT 'xpath',
    `created` datetime NOT NULL DEFAULT now(),
    `modified` datetime NOT NULL DEFAULT now(),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;

CREATE TABLE `field_config` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `module_type` int NOT NULL COMMENT '模块',
    `market_id` bigint NOT NULL COMMENT '市场id',
    `operator_id` bigint NOT NULL COMMENT '操作人ID',
    `operator_name` varchar(50) NOT NULL COMMENT '操作人姓名',
    `created` datetime NOT NULL DEFAULT now(),
    `modified` datetime NOT NULL DEFAULT now(),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;
ALTER TABLE dili_trace.field_config ADD CONSTRAINT moduletype_market_unique UNIQUE KEY (module_type,market_id);

CREATE TABLE `field_config_detail` (
       `id` bigint NOT NULL AUTO_INCREMENT,
       `field_config_id` bigint NOT NULL COMMENT '配置ID',
       `default_field_detail_id` bigint NOT NULL COMMENT '配置ID',
       `is_display` int NOT NULL DEFAULT 0 COMMENT '是否显示',
       `is_required` int NOT NULL DEFAULT 0 COMMENT '是否必填',
       `default_value` varchar(50)  NULL  COMMENT '默认值',
       `is_valid` int  NOT NULL COMMENT '是否有效',
       `created` datetime NOT NULL DEFAULT now(),
       `modified` datetime NOT NULL DEFAULT now(),
       PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;


