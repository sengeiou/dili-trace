ALTER TABLE dili_trace.`product_stock` ADD soft_weight decimal(17, 3) NOT NULL DEFAULT 0.000 COMMENT '锁定库存重量';

drop  table dili_trace.`message_config`;
drop  table dili_trace.`applets_config`;
