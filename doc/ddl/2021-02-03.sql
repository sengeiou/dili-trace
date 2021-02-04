

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



ALTER TABLE dili_trace.user_qr_history DROP COLUMN bill_id;
ALTER TABLE dili_trace.user_qr_history DROP COLUMN trade_request_id;

ALTER TABLE dili_trace.`user_qr_history` ADD qr_history_eventid bigint(20) NULL COMMENT 'qr事件ID';
ALTER TABLE dili_trace.`user_qr_history` ADD qr_history_event_type INT NULL COMMENT 'qr事件类型';
