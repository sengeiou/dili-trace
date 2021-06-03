ALTER TABLE dili_trace.`trade_detail` ADD detect_result int NOT NULL DEFAULT 0 COMMENT '检测结果';


ALTER TABLE dili_trace.`product_stock` ADD soft_weight decimal(17, 3) NOT NULL DEFAULT 0.000 COMMENT '锁定库存重量';



drop  table dili_trace.`message_config`;