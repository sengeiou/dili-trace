-- trace.approver_info definition

CREATE TABLE `approver_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) NOT NULL COMMENT '审核人名字',
  `user_id` bigint(20) NOT NULL COMMENT '审核人ID',
  `phone` varchar(20) DEFAULT NULL COMMENT '审核人电话',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;


-- trace.base64_signature definition

CREATE TABLE `base64_signature` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `approver_info_id` bigint(20) NOT NULL COMMENT '审核人ID',
  `base64` varchar(1000) NOT NULL COMMENT '审核人签名Base64图片',
  `order_num` int(11) NOT NULL COMMENT '顺序',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14265 DEFAULT CHARSET=utf8;


-- trace.biz_number definition

CREATE TABLE `biz_number` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL COMMENT '业务类型',
  `value` bigint(20) DEFAULT NULL COMMENT '编号值',
  `memo` varchar(50) DEFAULT NULL COMMENT '备注',
  `version` bigint(20) DEFAULT NULL COMMENT '版本号',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COMMENT='业务号\r\n记录所有业务的编号\r\n如：\r\n回访编号:HF201712080001';


-- trace.check_sheet definition

CREATE TABLE `check_sheet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(30) NOT NULL COMMENT '编号',
  `id_card_no` varchar(20) DEFAULT NULL COMMENT '提交人身份证号',
  `user_name` varchar(20) NOT NULL COMMENT '提交人姓名',
  `valid_period` int(11) NOT NULL COMMENT '有效天数',
  `detect_operator_id` bigint(20) NOT NULL COMMENT '检测人ID',
  `detect_operator_name` varchar(20) NOT NULL COMMENT '检测人姓名',
  `qrcode_url` varchar(150) NOT NULL COMMENT '二维码url',
  `approver_info_id` bigint(20) NOT NULL COMMENT '审核人ID',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `operator_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `operator_name` varchar(20) NOT NULL COMMENT '操作人姓名',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `task_type` int(11) NOT NULL DEFAULT '10' COMMENT '10登记单 20.委托登记单',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=95 DEFAULT CHARSET=utf8;


-- trace.check_sheet_detail definition

CREATE TABLE `check_sheet_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `check_sheet_id` bigint(20) NOT NULL COMMENT '检测报告单ID',
  `product_name` varchar(20) NOT NULL COMMENT '商品名称',
  `product_alias_name` varchar(20) DEFAULT NULL COMMENT '商品别名',
  `origin_name` varchar(20) NOT NULL COMMENT '产地',
  `order_number` int(11) NOT NULL COMMENT '序号',
  `detect_state` int(11) NOT NULL COMMENT '检测结果',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `latest_pd_result` varchar(100) DEFAULT NULL COMMENT '检测结果',
  `detect_task_id` bigint(20) DEFAULT NULL COMMENT '检测任务ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8;


/*进出门记录*/
CREATE TABLE `checkinout_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` int(11) NOT NULL COMMENT '进门状态',
  `inout` int(11) NOT NULL COMMENT '进出门',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- trace.code_generate definition

CREATE TABLE `code_generate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL COMMENT '编号类型',
  `segment` varchar(20) DEFAULT NULL COMMENT '当前编号段',
  `seq` bigint(20) DEFAULT NULL COMMENT '当前编号',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `pattern` varchar(20) DEFAULT NULL COMMENT '模式',
  `prefix` varchar(20) DEFAULT NULL COMMENT '前缀',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;


-- trace.commission_bill definition

CREATE TABLE `commission_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL COMMENT '编号',
  `name` varchar(50) DEFAULT NULL COMMENT '业户姓名',
  `corporate_name` varchar(100) DEFAULT NULL COMMENT '企业名称',
  `plate` varchar(15) DEFAULT NULL COMMENT '车牌',
  `state` tinyint(1) NOT NULL COMMENT '1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中，8.审核未通过',
  `product_name` varchar(20) NOT NULL COMMENT '商品名称',
  `product_id` bigint(20) NOT NULL,
  `origin_id` bigint(20) DEFAULT NULL COMMENT '产地ID',
  `origin_name` varchar(50) DEFAULT NULL COMMENT '产地',
  `weight` int(11) DEFAULT NULL COMMENT '重量',
  `tally_area_no` varchar(60) DEFAULT NULL COMMENT '理货区号',
  `trade_account` varchar(20) DEFAULT NULL COMMENT '交易区账号',
  `sample_code` varchar(255) DEFAULT NULL COMMENT '采样编号',
  `sample_source` tinyint(1) DEFAULT NULL COMMENT '1:采样检测 2:主动送检',
  `detect_state` tinyint(1) DEFAULT NULL COMMENT '默认null,1.合格 2.不合格 3.复检合格 4.复检不合格',
  `exe_machine_no` varchar(20) DEFAULT NULL COMMENT '检测仪器码',
  `latest_detect_record_id` bigint(20) DEFAULT NULL COMMENT '检测记录ID',
  `latest_detect_operator` varchar(20) DEFAULT NULL COMMENT '检测人员',
  `latest_detect_time` timestamp NULL DEFAULT NULL COMMENT '检测时间',
  `latest_pd_result` varchar(100) DEFAULT NULL COMMENT '最新一次检测值',
  `version` tinyint(4) NOT NULL DEFAULT '0' COMMENT '版本号',
  `creation_source` int(11) DEFAULT '10' COMMENT '数据来源',
  `check_sheet_id` bigint(20) DEFAULT NULL COMMENT '检测报告单ID',
  `operator_id` bigint(20) DEFAULT NULL,
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `id_card_no` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `commission_bill_unique_code` (`code`),
  KEY `index_commission_bill_exe_machine_no` (`exe_machine_no`),
  KEY `index_commission_bill_state` (`state`),
  KEY `index_commission_bill_sample_code` (`sample_code`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8;


-- trace.detect_record definition

CREATE TABLE `detect_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `detect_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '检测时间',
  `detect_operator` varchar(20) NOT NULL COMMENT '检测人',
  `detect_type` tinyint(1) NOT NULL COMMENT '1.第一次送检 2：复检',
  `detect_state` tinyint(1) NOT NULL COMMENT '1.合格 2.不合格',
  `pd_result` varchar(10) NOT NULL COMMENT '检测项的结果',
  `register_bill_code` varchar(20) NOT NULL COMMENT '登记单编号',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `REGISTER_BILL_CODE_INDEX` (`register_bill_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1093 DEFAULT CHARSET=utf8;


-- trace.detect_task definition

CREATE TABLE `detect_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NOT NULL COMMENT '登记单/委托登记单ID',
  `code` varchar(20) NOT NULL COMMENT '编号',
  `sample_code` varchar(20) NOT NULL COMMENT '采样编号',
  `task_type` int(11) NOT NULL COMMENT '10登记单 20.委托登记单',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65654 DEFAULT CHARSET=utf8;


-- trace.market definition

CREATE TABLE `market` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '市场名称',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- trace.quality_trace_trade_bill definition

CREATE TABLE `quality_trace_trade_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NOT NULL COMMENT '流水号',
  `register_bill_code` varchar(20) DEFAULT NULL,
  `sales_type` tinyint(1) DEFAULT NULL COMMENT '1.分销 2.全销',
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  `order_status` tinyint(1) DEFAULT NULL,
  `seller_account` varchar(20) DEFAULT NULL COMMENT '卖家账号',
  `seller_name` varchar(50) DEFAULT NULL COMMENT '卖家名称',
  `sellerIDNo` varchar(50) DEFAULT NULL,
  `buyer_account` varchar(20) DEFAULT NULL COMMENT '买家账号',
  `buyer_name` varchar(50) DEFAULT NULL COMMENT '买家名称',
  `buyerIDNo` varchar(50) DEFAULT NULL,
  `order_create_date` datetime DEFAULT NULL COMMENT '订单创建时间',
  `order_pay_date` datetime DEFAULT NULL COMMENT '订单支付时间',
  `pdResult` decimal(18,2) DEFAULT NULL COMMENT '残留值',
  `conclusion` tinyint(1) DEFAULT NULL COMMENT '合格值  0-未知 1合格  2不合格 3作废',
  `check_result_EID` bigint(20) DEFAULT NULL COMMENT '检测结算单唯一标志,NULL表示无检测信息',
  `trade_flow_id` bigint(20) DEFAULT NULL COMMENT '交易号',
  `total_money` bigint(30) DEFAULT NULL COMMENT '总金额',
  `order_item_id` bigint(20) DEFAULT NULL COMMENT '订单项号',
  `product_name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `cate_name` varchar(50) DEFAULT NULL COMMENT '品类名称',
  `price` bigint(30) DEFAULT NULL COMMENT '单价(分)',
  `piece_quantity` bigint(30) DEFAULT NULL COMMENT '件数',
  `piece_weight` bigint(20) DEFAULT NULL COMMENT '件重(公斤)',
  `net_weight` bigint(20) DEFAULT NULL COMMENT '总净重(公斤)',
  `tradetype_id` varchar(50) DEFAULT NULL COMMENT '交易类型编码',
  `tradetype_name` varchar(50) DEFAULT NULL COMMENT '交易类型名称',
  `bill_active` int(255) DEFAULT NULL COMMENT '状态',
  `sale_unit` int(1) DEFAULT NULL COMMENT '销售单位 1:斤 2：件',
  `match_status` int(1) DEFAULT NULL COMMENT '匹配状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `BILL_ID_UNIQUE_INDEX` (`bill_id`),
  KEY `ORDER_ID_INDEX` (`order_id`) USING BTREE,
  KEY `REGISTER_BILL_CODE_INDEX` (`register_bill_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1036863 DEFAULT CHARSET=utf8;


-- trace.quality_trace_trade_bill_syncpoint definition

CREATE TABLE `quality_trace_trade_bill_syncpoint` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) DEFAULT NULL COMMENT '流水号',
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1017880 DEFAULT CHARSET=utf8;


-- trace.r_user_upstream definition

CREATE TABLE `r_user_upstream` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户(商户)ID',
  `upstream_id` bigint(20) NOT NULL COMMENT '上游信息ID',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;


-- trace.register_bill definition

CREATE TABLE `register_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL COMMENT '编号',
  `register_source` tinyint(4) NOT NULL COMMENT '1.理货区 2.交易区',
  `tally_area_no` varchar(60) DEFAULT NULL COMMENT '理货区号',
  `name` varchar(50) DEFAULT NULL COMMENT '业户姓名',
  `id_card_no` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `addr` varchar(50) DEFAULT NULL COMMENT '地址',
  `phone` varchar(20) DEFAULT NULL COMMENT '业户手机号',
  `trade_account` varchar(20) DEFAULT NULL COMMENT '交易区账号',
  `trade_printing_card` varchar(50) DEFAULT NULL,
  `trade_type_id` varchar(10) DEFAULT NULL,
  `trade_type_name` varchar(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL COMMENT '理货区用户ID',
  `plate` varchar(15) NOT NULL COMMENT '车牌',
  `state` tinyint(1) NOT NULL COMMENT '1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中，8.审核未通过',
  `sales_type` tinyint(1) DEFAULT NULL COMMENT '1.分销 2.全销',
  `detect_report_url` varchar(4000) DEFAULT NULL,
  `product_name` varchar(20) NOT NULL COMMENT '商品名称',
  `product_id` bigint(20) NOT NULL,
  `origin_certifiy_url` varchar(4000) DEFAULT NULL,
  `origin_id` bigint(20) DEFAULT NULL COMMENT '产地ID',
  `origin_name` varchar(50) DEFAULT NULL COMMENT '产地',
  `weight` int(11) DEFAULT NULL COMMENT '重量',
  `operator_id` bigint(20) DEFAULT NULL,
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人',
  `sample_code` varchar(255) DEFAULT NULL COMMENT '采样编号',
  `sample_source` tinyint(1) DEFAULT NULL COMMENT '1:采样检测 2:主动送检',
  `detect_state` tinyint(1) DEFAULT NULL COMMENT '默认null,1.合格 2.不合格 3.复检合格 4.复检不合格',
  `latest_detect_record_id` bigint(20) DEFAULT NULL COMMENT '检测记录ID',
  `latest_detect_operator` varchar(20) DEFAULT NULL COMMENT '检测人员',
  `latest_detect_time` timestamp NULL DEFAULT NULL COMMENT '检测时间',
  `latest_pd_result` varchar(100) DEFAULT NULL COMMENT '最新一次检测值',
  `exe_machine_no` varchar(20) DEFAULT NULL COMMENT '检测仪器码',
  `handle_result_url` varchar(2000) DEFAULT NULL,
  `handle_result` varchar(10000) DEFAULT NULL,
  `version` tinyint(4) NOT NULL DEFAULT '0' COMMENT '版本号',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `creation_source` int(11) DEFAULT '10' COMMENT '数据来源',
  `check_sheet_id` bigint(20) DEFAULT NULL COMMENT '检测报告单ID',
  `separate_sales_record_id` bigint(20) DEFAULT NULL COMMENT '业户库存信息ID',
  `upstream_id` bigint(20) DEFAULT NULL COMMENT '上游信息ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `register_bill_unique_code` (`code`),
  KEY `index_register_bill_exe_machine_no` (`exe_machine_no`),
  KEY `index_register_bill_state` (`state`),
  KEY `index_register_bill_sample_code` (`sample_code`)
) ENGINE=InnoDB AUTO_INCREMENT=51301 DEFAULT CHARSET=utf8;


-- trace.separate_sales_record definition

CREATE TABLE `separate_sales_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sales_city_id` bigint(20) DEFAULT NULL COMMENT '分销城市',
  `sales_city_name` varchar(20) DEFAULT NULL COMMENT '分销城市',
  `sales_user_id` bigint(20) DEFAULT NULL,
  `sales_user_name` varchar(20) NOT NULL COMMENT '分销人',
  `sales_weight` int(11) NOT NULL COMMENT '分销重量KG',
  `sales_plate` varchar(255) DEFAULT NULL COMMENT '车牌',
  `register_bill_code` varchar(20) DEFAULT NULL COMMENT '被分销的登记单编码',
  `trade_no` varchar(20) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '分销自',
  `bill_id` bigint(20) DEFAULT NULL COMMENT '最初登记单ID',
  `store_weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '未分销重量',
  `sales_type` int(11) DEFAULT NULL COMMENT '分销类型',
  `checkin_record_id` bigint(20) DEFAULT NULL COMMENT '进门ID',
  `checkout_record_id` bigint(20) DEFAULT NULL COMMENT '出门ID',
  PRIMARY KEY (`id`),
  KEY `REGISTER_BILL_CODE_INDEX` (`register_bill_code`) USING BTREE,
  KEY `TRADE_NO_INDEX` (`trade_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=220 DEFAULT CHARSET=utf8;


-- trace.upstream definition

CREATE TABLE `upstream` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `upstream_type` int(11) NOT NULL COMMENT '10个人 20.企业',
  `id_card` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `telphone` varchar(20) NOT NULL COMMENT '联系方式',
  `name` varchar(20) NOT NULL COMMENT '企业(个人)名称',
  `legal_person` varchar(20) DEFAULT NULL COMMENT '法人姓名',
  `license` varchar(20) DEFAULT NULL COMMENT '统一信用代码',
  `business_license_url` varchar(100) DEFAULT NULL COMMENT '企业营业执照',
  `manufacturing_license_url` varchar(100) DEFAULT NULL COMMENT '生产许可证',
  `operation_license_url` varchar(100) DEFAULT NULL COMMENT '经营许可证',
  `card_no_front_url` varchar(100) DEFAULT NULL COMMENT '身份证照正面',
  `card_no_back_url` varchar(100) DEFAULT NULL COMMENT '身份证照反面URL',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `source_user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8;


-- trace.`user` definition

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `card_no` varchar(20) NOT NULL,
  `addr` varchar(50) NOT NULL,
  `card_no_front_url` varchar(100) DEFAULT NULL COMMENT '身份证正面',
  `card_no_back_url` varchar(100) DEFAULT NULL COMMENT '身份证反面',
  `tally_area_nos` varchar(60) NOT NULL,
  `business_license_url` varchar(100) DEFAULT NULL,
  `sales_city_id` bigint(20) DEFAULT NULL,
  `sales_city_name` varchar(20) DEFAULT NULL,
  `state` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:启用 0：禁用',
  `password` varchar(50) NOT NULL,
  `yn` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 -1：删除',
  `version` tinyint(4) NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` bigint(20) DEFAULT '0' COMMENT '是否删除',
  `qr_status` int(11) DEFAULT '0' COMMENT '二维码状态',
  `user_type` int(11) DEFAULT '10' COMMENT '用户类型',
  `market_id` bigint(20) DEFAULT NULL COMMENT '所属市场',
  `license` varchar(50) DEFAULT NULL COMMENT '统一信用代码',
  `legal_person` varchar(50) DEFAULT NULL COMMENT '法人姓名',
  `manufacturing_license_url` varchar(100) DEFAULT NULL COMMENT '生产许可证',
  `operation_license_url` varchar(100) DEFAULT NULL COMMENT '经营许可证',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_unique_phone` (`phone`,`is_delete`),
  UNIQUE KEY `user_unique_cardno` (`card_no`,`is_delete`)
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8;


-- trace.user_history definition

CREATE TABLE `user_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT 'ID',
  `name` varchar(30) NOT NULL COMMENT '名称',
  `phone` varchar(15) NOT NULL COMMENT '手机号',
  `card_no` varchar(20) NOT NULL COMMENT '身份证号',
  `addr` varchar(50) NOT NULL COMMENT '地址',
  `card_no_front_url` varchar(100) DEFAULT NULL COMMENT '身份证正面',
  `card_no_back_url` varchar(100) DEFAULT NULL COMMENT '身份证反面',
  `tally_area_nos` varchar(60) NOT NULL COMMENT '理货区号',
  `business_license_url` varchar(100) DEFAULT NULL COMMENT '营业执照URL',
  `sales_city_id` bigint(20) DEFAULT NULL COMMENT '销售城市ID',
  `sales_city_name` varchar(20) DEFAULT NULL COMMENT '销售城市名称',
  `state` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:启用 0：禁用',
  `password` varchar(50) NOT NULL COMMENT '密码',
  `user_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:业户 2：政府人员',
  `yn` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 -1：删除',
  `user_plates` varchar(800) DEFAULT NULL COMMENT '车牌',
  `plate_amount` int(11) NOT NULL DEFAULT '0' COMMENT '车牌数量',
  `version` tinyint(4) NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=688 DEFAULT CHARSET=utf8;


-- trace.user_plate definition

CREATE TABLE `user_plate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plate` varchar(20) NOT NULL COMMENT '车牌号',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `TALLY_AREA_NO_UNIQUE_INDEX` (`plate`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=448 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;


-- trace.user_product_store definition

CREATE TABLE `user_product_store` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '分销自',
  `user_product_store_type` int(11) NOT NULL COMMENT '库存来源类型',
  `user_id` bigint(20) DEFAULT NULL COMMENT '业户ID',
  `user_name` varchar(20) DEFAULT NULL COMMENT '业户姓名',
  `product_name` varchar(20) NOT NULL COMMENT '商品名称',
  `weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '重量',
  `store_weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '未分销重量',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- trace.user_qr_item definition

CREATE TABLE `user_qr_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户(商户)ID',
  `qr_item_type` int(11) NOT NULL COMMENT '二维码信息类型',
  `objects` varchar(500) DEFAULT NULL,
  `color` int(11) NOT NULL,
  `action` int(11) NOT NULL,
  `has_data` int(11) NOT NULL,
  `valid` int(11) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=316 DEFAULT CHARSET=utf8;


-- trace.user_tally_area definition

CREATE TABLE `user_tally_area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tally_area_no` varchar(20) NOT NULL COMMENT '理货区号',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `TALLY_AREA_NO_UNIQUE_INDEX` (`tally_area_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=900 DEFAULT CHARSET=utf8;


-- trace.usual_address definition

CREATE TABLE `usual_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '地址类型',
  `address_id` bigint(20) NOT NULL COMMENT '地址id',
  `address` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '地址',
  `merged_address` varchar(200) CHARACTER SET utf8 NOT NULL COMMENT '地址全名',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `today_used_count` int(11) DEFAULT '0' COMMENT '当天使用数量统计',
  `preday_used_count` int(11) DEFAULT '0' COMMENT '前一天使用数量统计',
  `clear_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '清理当天使用数量时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `usual_address_unique` (`address_id`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;