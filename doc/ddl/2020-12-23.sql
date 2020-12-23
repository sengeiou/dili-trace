
  ALTER TABLE dili_trace.register_bill_history ADD verify_operator_name varchar(20) NULL COMMENT '审核操作人姓名';
ALTER TABLE dili_trace.register_bill_history ADD verify_operator_id BIGINT NULL COMMENT '审核操作人ID';
ALTER TABLE dili_trace.register_bill_history ADD market_id BIGINT NULL COMMENT '市场id';


