
ALTER TABLE `code_generate`
ADD COLUMN `segment`  varchar(20) NULL COMMENT '当前编号段' AFTER `code`;

ALTER TABLE `code_generate`
ADD COLUMN `seq`  bigint(20) NULL COMMENT '当前编号' AFTER `segment`;


CREATE TABLE `usual_address` (
	id bigint(20) auto_increment NOT NULL,
	`type` varchar(20)  NOT NULL COMMENT '地址类型',
	`address_id` bigint(20) NOT NULL COMMENT '地址id',
	`address` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '地址',
	`merged_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT COMMENT '地址全名',
	`created` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
	`modified` timestamp DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP NOT NULL,
	CONSTRAINT `PRIMARY` PRIMARY KEY (id)
)