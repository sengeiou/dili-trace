ALTER TABLE `r_user_upstream` MODIFY COLUMN operator_name varchar(30)  NULL COMMENT '操作人ID';

ALTER TABLE `register_bill` MODIFY COLUMN operator_name varchar(30)  NULL COMMENT '操作人ID';
ALTER TABLE `checkinout_record` MODIFY COLUMN operator_name varchar(30)  NULL COMMENT '操作人ID';
ALTER TABLE `market` MODIFY COLUMN operator_name varchar(30)  NULL COMMENT '操作人ID';

ALTER TABLE `register_bill_history` MODIFY COLUMN operator_name varchar(30)  NULL COMMENT '操作人ID';
ALTER TABLE `trade_detail` MODIFY COLUMN buyer_name varchar(30)  NULL COMMENT '买家姓名';
ALTER TABLE `trade_detail` MODIFY COLUMN seller_name varchar(30)  NULL COMMENT '卖家姓名';


ALTER TABLE `trade_order` MODIFY COLUMN buyer_name varchar(30)  NULL COMMENT '买家姓名';
ALTER TABLE `trade_order` MODIFY COLUMN seller_name varchar(30)  NULL COMMENT '卖家姓名';

ALTER TABLE `trade_request` MODIFY COLUMN buyer_name varchar(30)  NULL COMMENT '买家姓名';
ALTER TABLE `trade_request` MODIFY COLUMN seller_name varchar(30)  NULL COMMENT '卖家姓名';

ALTER TABLE `upstream` MODIFY COLUMN operator_name varchar(30)  NULL COMMENT '操作人ID';

ALTER TABLE `user` MODIFY COLUMN tally_area_nos varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL;
