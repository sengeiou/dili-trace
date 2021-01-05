ALTER TABLE dili_trace.checkinout_record MODIFY COLUMN user_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '业户名称';
ALTER TABLE dili_trace.checkinout_record MODIFY COLUMN operator_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作人ID';
ALTER TABLE dili_trace.detect_record MODIFY COLUMN detect_operator varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '检测人';
ALTER TABLE dili_trace.register_bill MODIFY COLUMN operator_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作人ID';
ALTER TABLE dili_trace.register_bill_history MODIFY COLUMN operator_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '操作人ID';
ALTER TABLE dili_trace.register_bill_history MODIFY COLUMN verify_operator_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '审核操作人姓名';
ALTER TABLE dili_trace.separate_sales_record MODIFY COLUMN sales_user_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分销人';
ALTER TABLE dili_trace.seperate_print_report MODIFY COLUMN sales_user_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分销商名';
ALTER TABLE dili_trace.seperate_print_report MODIFY COLUMN operator_name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '操作人姓名';
ALTER TABLE dili_trace.trade_detail MODIFY COLUMN buyer_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL;
ALTER TABLE dili_trace.trade_detail MODIFY COLUMN seller_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL;
ALTER TABLE dili_trace.trade_order MODIFY COLUMN buyer_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL;
ALTER TABLE dili_trace.trade_order MODIFY COLUMN seller_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL;
ALTER TABLE dili_trace.`user` MODIFY COLUMN name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL;
ALTER TABLE dili_trace.user_history MODIFY COLUMN name varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称';
ALTER TABLE dili_trace.user_qr_history MODIFY COLUMN user_name varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL;
