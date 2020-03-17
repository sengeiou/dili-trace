ALTER TABLE `user_history` DROP COLUMN `user_type`;
ALTER TABLE `register_bill` ADD COLUMN `creation_source`  int default 10 COMMENT '数据创建来源';


ALTER TABLE `usual_address` ADD COLUMN `today_used_count`  int default 0 COMMENT '当天使用数量统计';
ALTER TABLE `usual_address` ADD COLUMN `preday_used_count`  int default 0 COMMENT '前一天使用数量统计';
ALTER TABLE `usual_address` ADD COLUMN `clear_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '清理当天使用数量时间';
