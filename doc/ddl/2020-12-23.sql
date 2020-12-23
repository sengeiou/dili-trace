
  ALTER TABLE dili_trace.register_bill_history ADD verify_operator_name varchar(20) NULL COMMENT '审核操作人姓名';
ALTER TABLE dili_trace.register_bill_history ADD verify_operator_id BIGINT NULL COMMENT '审核操作人ID';
ALTER TABLE dili_trace.register_bill_history ADD market_id BIGINT NULL COMMENT '市场id';
ALTER TABLE dili_trace.register_head ADD truck_type INT NULL COMMENT '拼车类型';
ALTER TABLE dili_trace.register_head ADD unit_price NUMERIC(10,3) NULL COMMENT '单价';




