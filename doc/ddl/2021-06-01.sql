ALTER TABLE dili_trace.`trade_detail` ADD detect_result int NOT NULL DEFAULT 0 COMMENT '检测结果';

drop  table dili_trace.`message_config`;