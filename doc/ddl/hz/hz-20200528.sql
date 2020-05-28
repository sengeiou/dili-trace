ALTER TABLE `checkinout_record` ADD COLUMN `user_name`  varchar(30)   NULL  COMMENT '业户名称';
ALTER TABLE `checkinout_record` ADD COLUMN `product_name` varchar(20)   NULL COMMENT '商品名称';
ALTER TABLE `checkinout_record` ADD COLUMN `sales_weight` int(11)  NULL COMMENT '商品重量';