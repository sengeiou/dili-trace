ALTER TABLE purchase_intention_record ADD code VARCHAR(20) NOT NULL COMMENT '买家报备编号';
ALTER TABLE purchase_intention_record ADD corporate_name VARCHAR(100) DEFAULT NULL COMMENT '企业名称';
ALTER TABLE purchase_intention_record ADD brand_id BIGINT(20) DEFAULT NULL COMMENT '品牌ID';
ALTER TABLE purchase_intention_record ADD brand_name VARCHAR(50) DEFAULT NULL COMMENT '品牌名称';
ALTER TABLE purchase_intention_record ADD origin_id BIGINT(20) DEFAULT NULL COMMENT '产地ID';
ALTER TABLE purchase_intention_record ADD origin_name VARCHAR(50) DEFAULT NULL COMMENT '产地';