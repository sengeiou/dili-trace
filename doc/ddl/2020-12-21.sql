ALTER TABLE dili_trace.truck_enter_record ADD market_id BIGINT NULL COMMENT '市场id';
ALTER TABLE dili_trace.truck_enter_record ADD driver_phone varchar(20) NULL COMMENT '司机电话';


ALTER TABLE dili_trace.purchase_intention_record ADD market_id bigint(20) NULL COMMENT '市场id';
ALTER TABLE dili_trace.purchase_intention_record ADD buyer_phone varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '买家电话';

ALTER TABLE dili_trace.purchase_intention_record ADD weight_unit INT NULL COMMENT '重量单位';
ALTER TABLE dili_trace.purchase_intention_record ADD product_weight NUMERIC(10,3) NOT NULL  DEFAULT 0 COMMENT '商品重量';
ALTER TABLE dili_trace.purchase_intention_record ADD plate VARCHAR (10)  NULL COMMENT '车牌';

ALTER TABLE dili_trace.register_bill ADD is_print_checksheet INT NULL COMMENT '是否打印';
ALTER TABLE dili_trace.register_bill ADD tare_weight NUMERIC(10,3) NOT NULL  DEFAULT 0 COMMENT '皮重';
