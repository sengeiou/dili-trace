-- dili_trace.trade_request_detail definition

CREATE TABLE `trade_request_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `trade_request_id` bigint DEFAULT NULL,
  `trade_detail_id` bigint DEFAULT NULL,
  `trade_weight` decimal(15,3) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `bill_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=213 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

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
ALTER TABLE dili_trace.trade_order MODIFY COLUMN order_status int NULL COMMENT '订单状态';

ALTER TABLE dili_trace.register_bill MODIFY COLUMN product_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称';
ALTER TABLE dili_trace.register_head MODIFY COLUMN product_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品名称';
ALTER TABLE dili_trace.product_stock MODIFY COLUMN product_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称';
ALTER TABLE dili_trace.check_sheet_detail MODIFY COLUMN product_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称';
ALTER TABLE dili_trace.check_sheet_detail MODIFY COLUMN product_alias_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品别名';
ALTER TABLE dili_trace.checkinout_record MODIFY COLUMN product_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称';
ALTER TABLE dili_trace.purchase_intention_record MODIFY COLUMN product_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品名称';
ALTER TABLE dili_trace.register_bill_history MODIFY COLUMN product_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '商品名称';
ALTER TABLE dili_trace.seperate_print_report MODIFY COLUMN product_alias_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品别名';
ALTER TABLE dili_trace.trade_request MODIFY COLUMN product_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '商品名称';

