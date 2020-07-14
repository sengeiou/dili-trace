
alter table `batch_stock` rename to `product_store`;
alter table `trade_detail` change `batch_stock_id`  `product_stock_id` bigint(20) NOT NULL COMMENT '商品库存ID';
alter table `trade_request` change `batch_stock_id`  `product_stock_id` bigint(20) NOT NULL COMMENT '商品库存ID';


/*batchStockId:productStockId*/
