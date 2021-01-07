-- truck_enter_record新增字段
ALTER TABLE `truck_enter_record` ADD COLUMN `code` varchar(20) NOT NULL COMMENT '编号' AFTER id;
ALTER TABLE `truck_enter_record` ADD COLUMN `corporate_name` VARCHAR(100) DEFAULT NULL COMMENT '企业名称' AFTER driver_name;