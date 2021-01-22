ALTER TABLE dili_trace.user_store ADD market_id BIGINT NULL COMMENT '市场ID';
ALTER TABLE dili_trace.user_store ADD market_name varchar(100) NULL COMMENT '市场名称';
ALTER TABLE dili_trace.detect_record MODIFY COLUMN pd_result varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '检测项的结果';
ALTER TABLE dili_trace.register_bill ADD return_reason varchar(100) NULL COMMENT '检测退回原因';
ALTER TABLE dili_trace.trade_detail ADD buyer_type INT DEFAULT 1 NOT NULL COMMENT '买家类型';
ALTER TABLE dili_trace.trade_request MODIFY COLUMN buyer_id bigint NULL COMMENT '买家ID';
ALTER TABLE dili_trace.trade_order MODIFY COLUMN buyer_id bigint NULL COMMENT '买家ID';


