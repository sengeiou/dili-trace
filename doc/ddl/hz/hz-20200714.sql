
alter table `batch_stock` rename to `product_stock`;
alter table `trade_detail` change `batch_stock_id`  `product_stock_id` bigint(20) NULL COMMENT '商品库存ID';
alter table `trade_request` change `batch_stock_id`  `product_stock_id` bigint(20) NULL COMMENT '商品库存ID';

ALTER TABLE `trade_detail` ADD `batch_no` varchar(60) NULL COMMENT '批次号';
ALTER TABLE `trade_detail` ADD `parent_batch_no` varchar(60) NULL COMMENT '父批次号';

update `trade_detail`  set `batch_no`=CONCAT(buyer_name," ",buyer_name);
update `trade_detail`  set `parent_batch_no`=(select CONCAT(name," ",created) from register_bill where trade_detail.bill_id=register_bill.id ) 
    where trade_type=0;

update `trade_detail` t1,`trade_detail` t2 set t1.parent_batch_no=t2.batch_no 
    where t1.parent_id =t2.id and t1.trade_type=10;
