-- 上传修改签名菜单 新增市场id
ALTER TABLE `approver_info`   
  ADD COLUMN `market_id` BIGINT(20) NULL  COMMENT '市场id';
