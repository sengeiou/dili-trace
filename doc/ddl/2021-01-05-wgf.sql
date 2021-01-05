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
-- purchase_intention_record
ALTER TABLE `purchase_intention_record` MODIFY `buyer_name` VARCHAR(50) DEFAULT NULL COMMENT '买家姓名'
ALTER TABLE `purchase_intention_record` MODIFY `operator_name` VARCHAR(50) DEFAULT NULL
-- approver_info
ALTER TABLE `approver_info` MODIFY `user_name` VARCHAR(50) NOT NULL COMMENT '审核人名字'
-- check_sheet
ALTER TABLE `check_sheet` MODIFY `user_name` VARCHAR(50) NOT NULL COMMENT '提交人姓名'
ALTER TABLE `check_sheet` MODIFY `detect_operator_name` VARCHAR(50) NOT NULL COMMENT '检测人姓名'
ALTER TABLE `check_sheet` MODIFY `operator_name` VARCHAR(50) NOT NULL COMMENT '操作人姓名'
-- r_user_upstream
ALTER TABLE `r_user_upstream` MODIFY `operator_name` VARCHAR(50) DEFAULT NULL COMMENT '操作人ID'
-- r_user_wechat
ALTER TABLE `r_user_wechat` MODIFY `user_name` VARCHAR(50) DEFAULT NULL COMMENT '用户名'
-- third_party_report_data
ALTER TABLE `third_party_report_data` MODIFY `operator_name` VARCHAR(50) DEFAULT NULL COMMENT '操作人姓名'
-- third_party_source_data
ALTER TABLE `third_party_source_data` MODIFY `operator_name` VARCHAR(50) DEFAULT NULL COMMENT '操作人姓名'
-- truck_enter_record
ALTER TABLE `truck_enter_record` MODIFY `driver_name` VARCHAR(50) NOT NULL COMMENT '司机姓名'
ALTER TABLE `truck_enter_record` MODIFY `operator_name` VARCHAR(50) DEFAULT NULL COMMENT '操作人'
-- upstream
ALTER TABLE `upstream` MODIFY `name` VARCHAR(50) NOT NULL COMMENT '企业(个人)名称'
ALTER TABLE `upstream` MODIFY `legal_person` VARCHAR(50) DEFAULT NULL COMMENT '法人姓名'
ALTER TABLE `upstream` MODIFY `operator_name` VARCHAR(50) DEFAULT NULL COMMENT '操作人姓名'