ALTER TABLE dili_trace.upstream DROP KEY upstream_UN;
ALTER TABLE dili_trace.register_bill_history ADD history_time DATETIME DEFAULT now() NOT NULL COMMENT '产生时间';

