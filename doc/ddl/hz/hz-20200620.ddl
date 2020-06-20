ALTER TABLE `register_bill` ADD COLUMN  `verify_status` int(11) not null default 0 COMMENT '查验状态';
ALTER TABLE `separate_sales_record` ADD COLUMN  `checkin_status` int(11) not null default 0 COMMENT '进门状态';
ALTER TABLE `separate_sales_record` ADD COLUMN  `checkout_status` int(11) not null default 0 COMMENT '出门状态';
ALTER TABLE `separate_sales_record` ADD COLUMN  `sale_status` int(11) not null default 0 COMMENT '交易销售状态';