ALTER TABLE brand ADD COLUMN market_id BIGINT(20) COMMENT '市场ID';
ALTER TABLE category ADD COLUMN market_id BIGINT(20) COMMENT '市场ID';
ALTER TABLE checkinout_record ADD COLUMN market_id BIGINT(20) COMMENT '市场ID';
ALTER TABLE product_stock ADD COLUMN market_id BIGINT(20) COMMENT '市场ID';
ALTER TABLE register_bill ADD COLUMN market_id BIGINT(20) COMMENT '市场ID';
ALTER TABLE sys_config ADD COLUMN market_id BIGINT(20) COMMENT '市场ID';
ALTER TABLE tally_area_no ADD COLUMN market_id BIGINT(20) COMMENT '市场ID';
ALTER TABLE third_party_push_data ADD COLUMN market_id BIGINT(20) COMMENT '市场ID';
ALTER TABLE third_party_report_data ADD COLUMN market_id BIGINT(20) COMMENT '市场ID';
ALTER TABLE trade_order ADD COLUMN buyer_market_id BIGINT(20) COMMENT '买家市场ID';
ALTER TABLE trade_order ADD COLUMN seller_market_id BIGINT(20) COMMENT '卖家市场ID';
ALTER TABLE trade_request ADD COLUMN buyer_market_id BIGINT(20) COMMENT '买家市场ID';
ALTER TABLE trade_request ADD COLUMN seller_market_id BIGINT(20) COMMENT '卖家市场ID';
ALTER TABLE trade_push_log ADD COLUMN market_id BIGINT(20) COMMENT '市场ID';
ALTER TABLE upstream ADD COLUMN market_id BIGINT(20) COMMENT '市场ID';

ALTER TABLE market ADD COLUMN app_id BIGINT(20);
ALTER TABLE market ADD COLUMN app_secret VARCHAR(50);
ALTER TABLE market ADD COLUMN context_url VARCHAR(50);
ALTER TABLE market ADD COLUMN platform_market_id BIGINT(20);

UPDATE brand SET market_id = 1;
UPDATE category SET market_id = 1;
UPDATE checkinout_record SET market_id = 1;
UPDATE product_stock SET market_id = 1;
UPDATE register_bill SET market_id = 1;
UPDATE sys_config SET market_id = 1;
UPDATE tally_area_no SET market_id = 1;
UPDATE third_party_push_data SET market_id = 1;
UPDATE third_party_report_data SET market_id = 1;
UPDATE trade_order SET buyer_market_id = 1;
UPDATE trade_order SET seller_market_id = 1;
UPDATE trade_request SET buyer_market_id = 1;
UPDATE trade_request SET seller_market_id = 1;
UPDATE trade_push_log SET market_id = 1;
UPDATE upstream SET market_id = 1;


INSERT INTO `market`(id, NAME, operator_id, operator_name, created, modified, app_id, app_secret, context_url, platform_market_id)
VALUES(1, "杭水", 260, "超级用户", NOW(), NOW(), 61835297, "baa05febaa330aa9ea5ccaa07ed140b24e82387f", "http://202.101.190.110:9008", 330110800);
INSERT INTO `market`(id, NAME, operator_id, operator_name, created, modified, app_id, app_secret, context_url, platform_market_id)
VALUES(2, "杭果", 260, "超级用户", NOW(), NOW(), 13254079, "0b21c361a44bc5eaf3f9f88db30288e3a3abb697", "http://202.101.190.110:9008", 330110800);


ALTER TABLE register_bill ADD COLUMN register_head_code VARCHAR(20) COMMENT '主台账编号';
ALTER TABLE register_bill ADD COLUMN third_party_code VARCHAR(20) COMMENT '经营户卡号';
ALTER TABLE register_bill ADD COLUMN area VARCHAR(20) COMMENT '区号';
ALTER TABLE register_bill ADD COLUMN measure_type tinyint(2) COMMENT '计量类型。10-计件 20-计重。默认计件。';
ALTER TABLE register_bill ADD COLUMN piece_num decimal(10,3) COMMENT '件数';
ALTER TABLE register_bill ADD COLUMN piece_weight decimal(10,3) COMMENT '件重';
ALTER TABLE register_bill ADD COLUMN remark VARCHAR(200) COMMENT '备注';
ALTER TABLE register_bill ADD COLUMN create_user VARCHAR(50) COMMENT '创建人';
ALTER TABLE register_bill ADD COLUMN delete_user VARCHAR(50) COMMENT '作废人';
ALTER TABLE register_bill ADD COLUMN delete_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '作废时间';
ALTER TABLE register_bill ADD COLUMN packaging VARCHAR(20) COMMENT '包装';
ALTER TABLE register_bill ADD COLUMN order_type tinyint(2) COMMENT '订单类型 1.报备单 2.进门登记单';


ALTER TABLE image_cert ADD COLUMN bill_type tinyint(2) COMMENT '单据类型。1-报备单 2-检测单 3-检测不合格处置单 4-进门主台账单。默认为1';
