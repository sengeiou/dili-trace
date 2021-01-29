ALTER TABLE dili_trace.trade_detail MODIFY COLUMN buyer_id bigint NULL COMMENT '买家ID';
ALTER TABLE dili_trace.trade_request MODIFY COLUMN seller_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL;
ALTER TABLE dili_trace.register_head MODIFY COLUMN piece_num decimal(11,3) DEFAULT 0.000 COMMENT '件数';
ALTER TABLE dili_trace.register_bill MODIFY COLUMN piece_num decimal(11,3) DEFAULT 0.000 COMMENT '件数';
