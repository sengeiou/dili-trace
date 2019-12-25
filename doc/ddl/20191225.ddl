
ALTER TABLE `code_generate`
ADD COLUMN `segment`  varchar(20) NULL COMMENT '当前编号段' AFTER `code`;

ALTER TABLE `code_generate`
ADD COLUMN `seq`  bigint(20) NULL COMMENT '当前编号' AFTER `segment`;