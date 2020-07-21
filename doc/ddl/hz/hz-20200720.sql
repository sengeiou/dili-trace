
ALTER TABLE `user_qr_history` ADD COLUMN `bill_id`  bigint(20) NULL COMMENT '报备单ID';
ALTER TABLE `user_qr_history` ADD COLUMN  `verify_status` int(11) not null default 0 COMMENT '查验状态';
ALTER TABLE `user_qr_history` ADD COLUMN  `is_valid` int(11) not null default 1 COMMENT '是否有效';

ALTER TABLE `register_bill` ADD COLUMN `is_deleted` int(11) not null default 0 COMMENT '是否被删除';

ALTER TABLE `register_bill_history` ADD COLUMN `is_deleted` int(11) not null default 0 COMMENT '是否被删除';


ALTER TABLE `checkinout_record` ADD COLUMN  `weight_unit` int(11) NOT NULL default 1 COMMENT '重量单位';
ALTER TABLE `checkinout_record` ADD COLUMN `user_id`  bigint(20) NULL COMMENT '业户ID';

/*DROP TABLE `user_access_log`;*/
