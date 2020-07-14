
alter table `batch_stock` rename to `product_stock`;
alter table `trade_detail` change `batch_stock_id`  `product_stock_id` bigint(20) NULL COMMENT '商品库存ID';
alter table `trade_request` change `batch_stock_id`  `product_stock_id` bigint(20) NULL COMMENT '商品库存ID';

ALTER TABLE `trade_detail` ADD `batch_no` varchar(20) NULL COMMENT '批次号';

update `trade_detail`  set `batch_no`=(select code from register_bill where trade_detail.bill_id=register_bill.id );

/*batchStockId:productStockId*/
