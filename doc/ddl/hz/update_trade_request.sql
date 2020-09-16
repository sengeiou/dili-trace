ALTER TABLE trade_request ADD COLUMN handle_time TIMESTAMP NULL;

UPDATE trade_request r, trade_order o SET r.handle_time=r.modified 
WHERE r.trade_order_id = o.id AND o.order_status !=0 AND r.return_status != 20; 