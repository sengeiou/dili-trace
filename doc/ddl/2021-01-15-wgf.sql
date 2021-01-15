ALTER TABLE dili_trace.r_user_upstream ADD user_name varchar(50) NULL COMMENT '用户名';

CREATE TABLE `bill_verify_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bill_id` bit(1) NOT NULL COMMENT '登记单ID',
  `verify_date_time` datetime DEFAULT NULL COMMENT '审核时间',
  `verify_operator_id` varchar(255) DEFAULT NULL COMMENT '审核人ID',
  `verify_operator_name` varchar(255) DEFAULT NULL COMMENT '审核人姓名',
  `previous_verify_status` varchar(255) DEFAULT NULL COMMENT '审核前状态值',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;