ALTER TABLE `register_bill` ADD COLUMN  `verify_state` int(11) not null default 0 COMMENT '查验状态';
ALTER TABLE `register_bill` ADD COLUMN  `checkin_status` int(11) not null default 0 COMMENT '进门状态';
ALTER TABLE `register_bill` ADD COLUMN  `checkout_status` int(11) not null default 0 COMMENT '出门状态';