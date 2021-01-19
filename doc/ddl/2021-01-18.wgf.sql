ALTER TABLE dili_trace.user_store ADD market_id BIGINT NULL COMMENT '市场ID';
ALTER TABLE dili_trace.user_store ADD market_name varchar(100) NULL COMMENT '市场名称';
ALTER TABLE dili_trace.detect_record MODIFY COLUMN pd_result varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '检测项的结果';
