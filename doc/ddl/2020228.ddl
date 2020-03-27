ALTER TABLE `user_history` DROP COLUMN `user_type`;
ALTER TABLE `register_bill` ADD COLUMN `creation_source`  int default 10 COMMENT '数据创建来源';


ALTER TABLE `usual_address` ADD COLUMN `today_used_count`  int default 0 COMMENT '当天使用数量统计';
ALTER TABLE `usual_address` ADD COLUMN `preday_used_count`  int default 0 COMMENT '前一天使用数量统计';
ALTER TABLE `usual_address` ADD COLUMN `clear_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '清理当天使用数量时间';



ALTER TABLE `register_bill` ADD COLUMN `check_sheet_id`  bigint(20) default null COMMENT '检测报告单ID';



CREATE TABLE `approver_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) NOT NULL COMMENT '审核人名字',  
  `user_id` bigint(20) NOT NULL COMMENT '审核人ID',  
  `phone` varchar(20) NOT NULL COMMENT '审核人电话', 
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

CREATE TABLE `base64_signature` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `approver_info_id` bigint(20) NOT NULL COMMENT '审核人ID',  
  `base64` varchar(1000) NOT NULL COMMENT '审核人签名Base64图片',
  `order_num` int NOT NULL COMMENT '顺序',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `check_sheet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(30) NOT NULL COMMENT '编号',
  `id_card_no`varchar(20) NOT NULL COMMENT '提交人身份证号',
  `user_name`varchar(20) NOT NULL COMMENT '提交人姓名',
  `valid_period` int NOT NULL COMMENT '有效天数',
  `detect_operator_id` bigint(20) NOT NULL COMMENT '检测人ID',
  `detect_operator_name`varchar(20) NOT NULL COMMENT '检测人姓名',
  `qrcode_url`varchar(150) NOT NULL COMMENT '二维码url',
  `approver_info_id`bigint(20) NOT NULL COMMENT '审核人ID',
  `remark`varchar(500) NULL COMMENT '备注',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


CREATE TABLE `check_sheet_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `check_sheet_id` bigint(20) NOT NULL COMMENT '检测报告单ID',
  `register_bill_id` bigint(20) NOT NULL COMMENT '登记单ID',
  
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `product_name`varchar(20) NOT NULL COMMENT '商品名称',
  `product_alias_name`varchar(20) NULL COMMENT '商品别名',
  
  `origin_id` bigint(20) NOT NULL COMMENT '产地ID',
  `origin_name` varchar(20) NOT NULL COMMENT '产地',
  
  `detect_state` int NOT NULL COMMENT '检测结果',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

update
	usual_address
inner join (
	SELECT
		origin_id,
		sum(case when DATEDIFF(now(),created)= 0 then 1 else 0 end) today_used_count ,
		sum(case when DATEDIFF(now(),created)= 1 then 1 else 0 end) preday_used_count ,
		'register' AS `type`
	from
		register_bill
	where DATEDIFF(now(), created)= 0 or DATEDIFF(now(), created)= 1
	group by
		origin_id
		
	union
	
	SELECT
		sales_city_id,
		sum(case when DATEDIFF(now(),created)= 0 then 1 else 0 end) today_used_count ,
		sum(case when DATEDIFF(now(),created)= 1 then 1 else 0 end) preday_used_count ,
		'user' AS `type`
	from
		`user`
	where
		DATEDIFF(now(),created)= 0	or DATEDIFF(now(),created)= 1
	group by
		sales_city_id )t 
	on	usual_address.type = t.type and t.origin_id = address_id
	set usual_address.today_used_count = t.today_used_count,
	usual_address.preday_used_count = t.preday_used_count;
