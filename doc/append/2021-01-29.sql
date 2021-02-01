ALTER TABLE dili_trace.trade_detail MODIFY COLUMN buyer_id bigint NULL COMMENT '买家ID';
ALTER TABLE dili_trace.trade_request MODIFY COLUMN seller_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL;
ALTER TABLE dili_trace.register_head MODIFY COLUMN piece_num decimal(11,3) DEFAULT 0.000 COMMENT '件数';
ALTER TABLE dili_trace.register_bill MODIFY COLUMN piece_num decimal(11,3) DEFAULT 0.000 COMMENT '件数';
ALTER TABLE dili_trace.`user` ADD user_id BIGINT NULL;
ALTER TABLE dili_trace.`user` ADD last_sync_time DATETIME NULL COMMENT '最后的同步时间';
ALTER TABLE dili_trace.`user` ADD last_sync_success INT NULL COMMENT '最后是否同步成功(1:成功,0:失败)';
ALTER TABLE dili_trace.`user` MODIFY COLUMN card_no varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL;
ALTER TABLE dili_trace.`user` MODIFY COLUMN phone varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL;
ALTER TABLE dili_trace.`user` MODIFY COLUMN password varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL;
ALTER TABLE dili_trace.`user` MODIFY COLUMN state tinyint(1) DEFAULT 1 NULL COMMENT '1:启用 0：禁用';
ALTER TABLE dili_trace.`user` MODIFY COLUMN modified DATETIME NULL;
ALTER TABLE dili_trace.`user` MODIFY COLUMN created DATETIME NULL;
ALTER TABLE dili_trace.`user` MODIFY COLUMN validate_state tinyint DEFAULT 10 NULL COMMENT '未实名10 待审核 20  审核未通过 30 审核通过 40';
ALTER TABLE dili_trace.`user` MODIFY COLUMN name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL;
ALTER TABLE dili_trace.trade_request MODIFY COLUMN reason varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '原因';

ALTER TABLE dili_trace.`user` DROP KEY user_unique_phone;
ALTER TABLE dili_trace.`user` ADD CONSTRAINT user_id_market_id_unique UNIQUE KEY (market_id,user_id);

ALTER TABLE dili_trace.trade_request ADD order_status INT NULL COMMENT '状态';


DROP TABLE IF EXISTS category;
CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上一级ID',
  `category_id` bigint(20) DEFAULT NULL COMMENT 'categoryID',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `full_name` varchar(20) DEFAULT NULL COMMENT '全名',
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






