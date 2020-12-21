ALTER TABLE dili_trace.truck_enter_record ADD market_id BIGINT NULL COMMENT '市场id';
ALTER TABLE dili_trace.truck_enter_record ADD driver_phone varchar(20) NULL COMMENT '司机电话';


ALTER TABLE dili_trace.purchase_intention_record ADD market_id bigint(20) NULL COMMENT '市场id';
ALTER TABLE dili_trace.purchase_intention_record ADD buyer_phone varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '买家电话';
