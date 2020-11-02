/*
SQLyog Ultimate v12.2.6 (64 bit)
MySQL - 5.7.17 : Database - hztrace1021
*********************************************************************
*/
/*Table structure for table `DATABASECHANGELOGLOCK` */

DROP TABLE IF EXISTS `DATABASECHANGELOGLOCK`;

CREATE TABLE `DATABASECHANGELOGLOCK` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Table structure for table `applets_config` */

DROP TABLE IF EXISTS `applets_config`;

CREATE TABLE `applets_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(50) DEFAULT NULL COMMENT '小程序名称',
  `app_id` varchar(50) DEFAULT NULL COMMENT '小程序id',
  `app_secret` varchar(100) DEFAULT NULL COMMENT '小程序Secret',
  `access_token` varchar(1000) DEFAULT NULL COMMENT '小程序accessToken',
  `access_token_expires_in` int(10) DEFAULT NULL COMMENT '小程序accessToken有效时间',
  `access_token_update_time` datetime DEFAULT NULL COMMENT '小程序accessToken更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Table structure for table `approver_info` */

DROP TABLE IF EXISTS `approver_info`;

CREATE TABLE `approver_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) NOT NULL COMMENT '审核人名字',
  `user_id` bigint(20) NOT NULL COMMENT '审核人ID',
  `phone` varchar(20) DEFAULT NULL COMMENT '审核人电话',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `base64_signature` */

DROP TABLE IF EXISTS `base64_signature`;

CREATE TABLE `base64_signature` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `approver_info_id` bigint(20) NOT NULL COMMENT '审核人ID',
  `base64` varchar(1000) NOT NULL COMMENT '审核人签名Base64图片',
  `order_num` int(11) NOT NULL COMMENT '顺序',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `biz_number` */

DROP TABLE IF EXISTS `biz_number`;

CREATE TABLE `biz_number` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) NOT NULL COMMENT '业务类型',
  `value` bigint(20) DEFAULT NULL COMMENT '编号值',
  `memo` varchar(50) DEFAULT NULL COMMENT '备注',
  `version` bigint(20) DEFAULT NULL COMMENT '版本号',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='业务号\r\n记录所有业务的编号\r\n如：\r\n回访编号:HF201712080001';

/*Table structure for table `brand` */

DROP TABLE IF EXISTS `brand`;

CREATE TABLE `brand` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `brand_name` varchar(50) DEFAULT NULL COMMENT '品牌名称',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

/*Table structure for table `category` */

DROP TABLE IF EXISTS `category`;

CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上一级ID',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `full_name` varchar(20) DEFAULT NULL COMMENT '全名',
  `level` int(11) NOT NULL DEFAULT '1' COMMENT '层级',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=231 DEFAULT CHARSET=utf8;

/*Table structure for table `checkinout_record` */

DROP TABLE IF EXISTS `checkinout_record`;

CREATE TABLE `checkinout_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` int(11) NOT NULL COMMENT '进门状态',
  `inout` int(11) NOT NULL COMMENT '进出门',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(30) DEFAULT NULL COMMENT '操作人ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_name` varchar(30) DEFAULT NULL COMMENT '业户名称',
  `product_name` varchar(20) NOT NULL COMMENT '商品名称',
  `inout_weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '进出门重量',
  `trade_detail_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '分销ID',
  `verify_status` int(11) NOT NULL DEFAULT '0' COMMENT '查验状态',
  `bill_type` int(11) NOT NULL DEFAULT '10' COMMENT '报备类型',
  `bill_id` bigint(20) DEFAULT NULL COMMENT '报备单ID',
  `weight_unit` int(11) NOT NULL DEFAULT '1' COMMENT '重量单位',
  `user_id` bigint(20) DEFAULT NULL COMMENT '业户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2711 DEFAULT CHARSET=utf8;

/*Table structure for table `code_generate` */

DROP TABLE IF EXISTS `code_generate`;

CREATE TABLE `code_generate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL COMMENT '编号类型',
  `segment` varchar(20) DEFAULT NULL COMMENT '当前编号段',
  `seq` bigint(20) DEFAULT NULL COMMENT '当前编号',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `pattern` varchar(20) DEFAULT NULL COMMENT '模式',
  `prefix` varchar(20) DEFAULT NULL COMMENT '前缀',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

/*Table structure for table `event_message` */

DROP TABLE IF EXISTS `event_message`;

CREATE TABLE `event_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(20) DEFAULT NULL,
  `overview` varchar(50) DEFAULT NULL,
  `content` varchar(150) DEFAULT NULL,
  `source_business_id` bigint(20) DEFAULT NULL COMMENT '关联业务id',
  `source_business_type` tinyint(2) NOT NULL COMMENT '关联业务类型 用户 10 报备 20 交易 30 ;',
  `creator_id` bigint(20) DEFAULT NULL,
  `creator` varchar(50) DEFAULT NULL,
  `receiver_id` bigint(20) DEFAULT NULL COMMENT '消息接收者id',
  `receiver` varchar(50) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `read_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '已读标志 未读0 已读 1',
  `receiver_type` tinyint(2) NOT NULL COMMENT '接收者角色 普通用户10 管理员20',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8589 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `image_cert` */

DROP TABLE IF EXISTS `image_cert`;

CREATE TABLE `image_cert` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) DEFAULT NULL COMMENT '所属数据ID',
  `url` varchar(200) DEFAULT NULL COMMENT '图片URL',
  `cert_type` int(11) NOT NULL COMMENT '图片类型',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46121 DEFAULT CHARSET=utf8;

/*Table structure for table `market` */

DROP TABLE IF EXISTS `market`;

CREATE TABLE `market` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '市场名称',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(30) DEFAULT NULL COMMENT '操作人ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `message_config` */

DROP TABLE IF EXISTS `message_config`;

CREATE TABLE `message_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operation` tinyint(4) DEFAULT NULL COMMENT '10:商户注册提交;11:商户注册审核通过;12:商户注册审核未通过;20:提交报备;21:报备审核通过;22:报备审核未通过;23:报备审核退回;30:进门审核;40:卖家下单;50:买家下单;51:卖家确认订单',
  `message_flag` char(1) DEFAULT NULL COMMENT '是否发站内信',
  `sms_flag` char(1) DEFAULT NULL COMMENT '是否发短信',
  `wechat_flag` char(1) DEFAULT NULL COMMENT '是否发微信',
  `sms_scene_code` varchar(20) DEFAULT NULL COMMENT '短信中心sceneCode',
  `wechat_template_id` varchar(50) DEFAULT NULL COMMENT '微信模板id',
  `event_message_title` varchar(50) DEFAULT NULL COMMENT '站内消息标题',
  `event_message_content` varchar(200) DEFAULT NULL COMMENT '站内消息内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

/*Table structure for table `product_stock` */

DROP TABLE IF EXISTS `product_stock`;

CREATE TABLE `product_stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `preserve_type` int(11) NOT NULL DEFAULT '10' COMMENT '保存类型',
  `stock_weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '库存重量',
  `total_weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '总重量',
  `weight_unit` int(11) NOT NULL DEFAULT '10' COMMENT '重量单位',
  `spec_name` varchar(20) DEFAULT NULL COMMENT '规格',
  `product_name` varchar(20) NOT NULL COMMENT '商品名称',
  `product_id` bigint(20) NOT NULL,
  `user_name` varchar(50) DEFAULT NULL COMMENT '业户姓名',
  `user_id` bigint(20) DEFAULT NULL COMMENT '理货区用户ID',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `trade_detail_num` int(11) NOT NULL DEFAULT '0' COMMENT '可用批次数量',
  `brand_name` varchar(50) DEFAULT NULL COMMENT '品牌名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1307 DEFAULT CHARSET=utf8;

/*Table structure for table `r_user_category` */

DROP TABLE IF EXISTS `r_user_category`;

CREATE TABLE `r_user_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `category_name` varchar(100) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `category_type` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=239 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `r_user_tally_area` */

DROP TABLE IF EXISTS `r_user_tally_area`;

CREATE TABLE `r_user_tally_area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tally_area_no_id` bigint(20) DEFAULT NULL COMMENT '摊位号ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1010 DEFAULT CHARSET=utf8;

/*Table structure for table `r_user_upstream` */

DROP TABLE IF EXISTS `r_user_upstream`;

CREATE TABLE `r_user_upstream` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户(商户)ID',
  `upstream_id` bigint(20) NOT NULL COMMENT '上游信息ID',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(30) DEFAULT NULL COMMENT '操作人ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=271 DEFAULT CHARSET=utf8;

/*Table structure for table `register_bill` */

DROP TABLE IF EXISTS `register_bill`;

CREATE TABLE `register_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL COMMENT '编号',
  `sample_code` varchar(20) DEFAULT NULL COMMENT '采样编号',
  `name` varchar(50) DEFAULT NULL COMMENT '业户姓名',
  `id_card_no` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `addr` varchar(50) DEFAULT NULL COMMENT '地址',
  `phone` varchar(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL COMMENT '理货区用户ID',
  `plate` varchar(15) DEFAULT NULL COMMENT '车牌',
  `state` tinyint(1) NOT NULL COMMENT '1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中，8.审核未通过',
  `sales_type` tinyint(1) DEFAULT NULL COMMENT '1.分销 2.全销',
  `product_name` varchar(20) NOT NULL COMMENT '商品名称',
  `product_id` bigint(20) NOT NULL,
  `origin_id` bigint(20) DEFAULT NULL COMMENT '产地ID',
  `origin_name` varchar(50) DEFAULT NULL COMMENT '产地',
  `weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '重量',
  `operator_id` bigint(20) DEFAULT NULL,
  `operator_name` varchar(30) DEFAULT NULL COMMENT '操作人ID',
  `sample_source` tinyint(1) DEFAULT NULL COMMENT '1:采样检测 2:主动送检',
  `detect_state` tinyint(1) DEFAULT NULL COMMENT '默认null,1.合格 2.不合格 3.复检合格 4.复检不合格',
  `latest_detect_record_id` bigint(20) DEFAULT NULL COMMENT '检测记录ID',
  `latest_detect_operator` varchar(20) DEFAULT NULL COMMENT '检测人员',
  `latest_detect_time` timestamp NULL DEFAULT NULL COMMENT '检测时间',
  `latest_pd_result` varchar(100) DEFAULT NULL COMMENT '最新一次检测值',
  `version` tinyint(4) NOT NULL DEFAULT '0' COMMENT '版本号',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `upstream_id` bigint(20) DEFAULT NULL COMMENT '上游信息ID',
  `complete` int(11) DEFAULT NULL COMMENT '信息是否完整',
  `verify_status` int(11) NOT NULL DEFAULT '0' COMMENT '查验状态, "待审核"0, "已退回10, "已通过20, "不通过30',
  `weight_unit` int(11) NOT NULL DEFAULT '1' COMMENT '重量单位',
  `preserve_type` int(11) NOT NULL DEFAULT '10' COMMENT '保存类型',
  `verify_type` int(11) NOT NULL DEFAULT '0' COMMENT '查验类型',
  `spec_name` varchar(20) DEFAULT NULL COMMENT '规格',
  `bill_type` int(11) NOT NULL DEFAULT '10' COMMENT '报备类型',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌ID',
  `brand_name` varchar(50) DEFAULT NULL COMMENT '品牌名称',
  `truck_type` int(11) NOT NULL DEFAULT '10' COMMENT '车类型',
  `tally_area_no` varchar(60) DEFAULT NULL COMMENT '摊位号',
  `reason` varchar(100) DEFAULT NULL COMMENT '原因',
  `is_checkin` int(11) NOT NULL DEFAULT '-1' COMMENT '是否进门',
  `checkin_status` int(11) NOT NULL DEFAULT '0' COMMENT '进门状态',
  `checkout_status` int(11) NOT NULL DEFAULT '0' COMMENT '出门状态',
  `is_deleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否被删除',
  `operation_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `register_bill_unique_code` (`code`),
  KEY `ix_created` (`created`,`product_name`)
) ENGINE=InnoDB AUTO_INCREMENT=39215 DEFAULT CHARSET=utf8;

/*Table structure for table `register_bill_history` */

DROP TABLE IF EXISTS `register_bill_history`;

CREATE TABLE `register_bill_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NOT NULL,
  `code` varchar(20) NOT NULL COMMENT '编号',
  `sample_code` varchar(20) DEFAULT NULL COMMENT '采样编号',
  `name` varchar(50) DEFAULT NULL COMMENT '业户姓名',
  `id_card_no` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `addr` varchar(50) DEFAULT NULL COMMENT '地址',
  `phone` varchar(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL COMMENT '理货区用户ID',
  `plate` varchar(15) DEFAULT NULL COMMENT '车牌',
  `state` tinyint(1) NOT NULL COMMENT '1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中，8.审核未通过',
  `sales_type` tinyint(1) DEFAULT NULL COMMENT '1.分销 2.全销',
  `product_name` varchar(20) NOT NULL COMMENT '商品名称',
  `product_id` bigint(20) NOT NULL,
  `origin_id` bigint(20) DEFAULT NULL COMMENT '产地ID',
  `origin_name` varchar(50) DEFAULT NULL COMMENT '产地',
  `weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '重量',
  `operator_id` bigint(20) DEFAULT NULL,
  `operator_name` varchar(30) DEFAULT NULL COMMENT '操作人ID',
  `sample_source` tinyint(1) DEFAULT NULL COMMENT '1:采样检测 2:主动送检',
  `detect_state` tinyint(1) DEFAULT NULL COMMENT '默认null,1.合格 2.不合格 3.复检合格 4.复检不合格',
  `latest_detect_record_id` bigint(20) DEFAULT NULL COMMENT '检测记录ID',
  `latest_detect_operator` varchar(20) DEFAULT NULL COMMENT '检测人员',
  `latest_detect_time` timestamp NULL DEFAULT NULL COMMENT '检测时间',
  `latest_pd_result` varchar(100) DEFAULT NULL COMMENT '最新一次检测值',
  `version` tinyint(4) NOT NULL DEFAULT '0' COMMENT '版本号',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `upstream_id` bigint(20) DEFAULT NULL COMMENT '上游信息ID',
  `complete` int(11) DEFAULT NULL COMMENT '信息是否完整',
  `verify_status` int(11) NOT NULL DEFAULT '0' COMMENT '查验状态',
  `weight_unit` int(11) NOT NULL DEFAULT '1' COMMENT '重量单位',
  `preserve_type` int(11) NOT NULL DEFAULT '10' COMMENT '保存类型',
  `verify_type` int(11) NOT NULL DEFAULT '0' COMMENT '查验类型',
  `spec_name` varchar(20) DEFAULT NULL COMMENT '规格',
  `bill_type` int(11) NOT NULL DEFAULT '10' COMMENT '报备类型',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌ID',
  `brand_name` varchar(50) DEFAULT NULL COMMENT '品牌名称',
  `truck_type` int(11) NOT NULL DEFAULT '10' COMMENT '车类型',
  `tally_area_no` varchar(60) DEFAULT NULL COMMENT '摊位号',
  `reason` varchar(100) DEFAULT NULL COMMENT '原因',
  `is_checkin` int(11) NOT NULL DEFAULT '-1' COMMENT '是否进门',
  `is_deleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否被删除',
  `operation_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54968 DEFAULT CHARSET=utf8;

/*Table structure for table `separate_sales_record` */

DROP TABLE IF EXISTS `separate_sales_record`;

CREATE TABLE `separate_sales_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sales_city_id` bigint(20) DEFAULT NULL COMMENT '分销城市',
  `sales_city_name` varchar(20) DEFAULT NULL COMMENT '分销城市',
  `sales_user_id` bigint(20) DEFAULT NULL,
  `sales_user_name` varchar(20) NOT NULL COMMENT '分销人',
  `sales_plate` varchar(15) DEFAULT NULL COMMENT '分销车牌号',
  `sales_weight` int(11) NOT NULL COMMENT '分销重量KG',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `sms_message` */

DROP TABLE IF EXISTS `sms_message`;

CREATE TABLE `sms_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source_business_type` tinyint(4) DEFAULT NULL COMMENT '关联业务类型 用户 10 报备 20 交易 30',
  `source_business_id` bigint(20) DEFAULT NULL COMMENT '关联业务单据id',
  `receive_phone` varchar(20) DEFAULT NULL COMMENT '接收短信手机号码',
  `send_reason` tinyint(4) DEFAULT NULL,
  `result_code` varchar(50) DEFAULT NULL COMMENT '短信发送返回结果码',
  `result_info` varchar(2000) DEFAULT NULL COMMENT '短信发送返回结果信息',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=358 DEFAULT CHARSET=utf8;

/*Table structure for table `sys_config` */

DROP TABLE IF EXISTS `sys_config`;

CREATE TABLE `sys_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `instructions` varchar(50) DEFAULT NULL COMMENT '说明',
  `opt_type` varchar(50) DEFAULT NULL COMMENT '配置类型',
  `opt_category` varchar(50) DEFAULT NULL COMMENT '配置分类',
  `opt_value` varchar(200) DEFAULT NULL COMMENT '配置参数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `tally_area_no` */

DROP TABLE IF EXISTS `tally_area_no`;

CREATE TABLE `tally_area_no` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `number` varchar(50) DEFAULT NULL COMMENT '摊位号',
  `street` varchar(30) DEFAULT NULL COMMENT '街区号',
  `area` varchar(30) NOT NULL COMMENT '区域',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1542 DEFAULT CHARSET=utf8;

/*Table structure for table `third_party_push_data` */

DROP TABLE IF EXISTS `third_party_push_data`;

CREATE TABLE `third_party_push_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `interface_name` varchar(50) NOT NULL,
  `table_name` varchar(50) NOT NULL,
  `push_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

/*Table structure for table `third_party_report_data` */

DROP TABLE IF EXISTS `third_party_report_data`;

CREATE TABLE `third_party_report_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) DEFAULT NULL COMMENT '上报名称',
  `type` int(11) DEFAULT NULL COMMENT '上报类型',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人ID',
  `data` text COMMENT '提交数据结果',
  `success` int(11) DEFAULT NULL COMMENT '是否成功执行',
  `msg` varchar(150) DEFAULT NULL COMMENT '执行结果',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=69393 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `trade_detail` */

DROP TABLE IF EXISTS `trade_detail`;

CREATE TABLE `trade_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '分销来源ID',
  `bill_id` bigint(20) NOT NULL COMMENT '登记单ID',
  `checkin_record_id` bigint(20) DEFAULT NULL COMMENT '进门ID',
  `checkout_record_id` bigint(20) DEFAULT NULL COMMENT '进门ID',
  `checkin_status` int(11) NOT NULL COMMENT '进门状态',
  `checkout_status` int(11) NOT NULL COMMENT '出门状态',
  `sale_status` int(11) NOT NULL COMMENT '交易状态',
  `trade_type` int(11) NOT NULL COMMENT '交易类型',
  `buyer_id` bigint(20) NOT NULL COMMENT '买家ID',
  `buyer_name` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL,
  `seller_id` bigint(20) DEFAULT NULL COMMENT '卖家ID',
  `seller_name` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL,
  `product_name` varchar(50) NOT NULL COMMENT '商品名称',
  `stock_weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '库存重量',
  `total_weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '总重量',
  `weight_unit` int(11) NOT NULL DEFAULT '10' COMMENT '重量单位',
  `product_stock_id` bigint(20) DEFAULT NULL COMMENT '商品库存ID',
  `trade_request_id` bigint(20) DEFAULT NULL COMMENT '交易请求ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_batched` int(11) NOT NULL DEFAULT '0' COMMENT '是否计算入BatchStock',
  `batch_no` varchar(60) DEFAULT NULL COMMENT '批次号',
  `parent_batch_no` varchar(60) DEFAULT NULL COMMENT '父批次号',
  `pushaway_weight` decimal(10,3) DEFAULT '0.000',
  `soft_weight` decimal(10,3) DEFAULT '0.000',
  PRIMARY KEY (`id`),
  KEY `index_trade_detail_bill_id` (`checkin_record_id`),
  KEY `bill_id` (`bill_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8286 DEFAULT CHARSET=utf8;

/*Table structure for table `trade_order` */

DROP TABLE IF EXISTS `trade_order`;

CREATE TABLE `trade_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_status` int(11) NOT NULL COMMENT '订单状态',
  `order_type` int(11) NOT NULL COMMENT '订单类型',
  `buyer_id` bigint(20) NOT NULL COMMENT '买家ID',
  `buyer_name` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL,
  `seller_id` bigint(20) DEFAULT NULL COMMENT '卖家ID',
  `seller_name` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=479 DEFAULT CHARSET=utf8;

/*Table structure for table `trade_push_log` */

DROP TABLE IF EXISTS `trade_push_log`;

CREATE TABLE `trade_push_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trade_detail_id` bigint(20) DEFAULT NULL COMMENT '批次主键',
  `log_type` tinyint(4) DEFAULT NULL COMMENT '0:下架 1:上架',
  `product_name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `operation_weight` decimal(10,3) DEFAULT NULL COMMENT '上下架重量',
  `order_type` tinyint(4) DEFAULT NULL COMMENT '0：报备单 10：交易单',
  `order_id` bigint(20) DEFAULT NULL COMMENT '单据主键id。报备单id或者交易单id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '商户 id',
  `product_stock_id` bigint(20) DEFAULT NULL COMMENT '库存 id',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `operation_reason` varchar(200) DEFAULT NULL,
  `order_code` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `INDEX_TRADE_DETAIL_ID` (`user_id`,`trade_detail_id`),
  KEY `INDEX_USER_ID` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

/*Table structure for table `trade_request` */

DROP TABLE IF EXISTS `trade_request`;

CREATE TABLE `trade_request` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `buyer_id` bigint(20) NOT NULL COMMENT '买家ID',
  `buyer_name` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL,
  `seller_id` bigint(20) DEFAULT NULL COMMENT '卖家ID',
  `seller_name` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL,
  `trade_weight` decimal(10,3) NOT NULL COMMENT '交易重量',
  `product_stock_id` bigint(20) DEFAULT NULL COMMENT '商品库存ID',
  `trade_order_Id` bigint(20) NOT NULL COMMENT '订单ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `return_status` int(11) DEFAULT NULL COMMENT '退货状态',
  `reason` varchar(200) DEFAULT NULL COMMENT '原因',
  `code` varchar(50) DEFAULT NULL COMMENT '编号',
  `weight_unit` int(11) NOT NULL DEFAULT '10' COMMENT '重量单位',
  `spec_name` varchar(20) DEFAULT NULL COMMENT '规格',
  `product_name` varchar(20) DEFAULT NULL COMMENT '商品名称',
  `handle_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10489 DEFAULT CHARSET=utf8;

/*Table structure for table `upstream` */

DROP TABLE IF EXISTS `upstream`;

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
  `source_user_id` bigint(20) DEFAULT NULL COMMENT '复制来源userid',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(30) DEFAULT NULL COMMENT '操作人ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `upORdown` tinyint(2) DEFAULT '10' COMMENT '上游企业10 下游企业20',
  PRIMARY KEY (`id`),
  UNIQUE KEY `upstream_UN` (`telphone`,`upORdown`,`source_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=215 DEFAULT CHARSET=utf8;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(36) CHARACTER SET utf8mb4 NOT NULL,
  `phone` varchar(15) NOT NULL,
  `card_no` varchar(20) NOT NULL,
  `addr` varchar(50) DEFAULT NULL,
  `card_no_front_url` varchar(100) DEFAULT NULL COMMENT '身份证正面',
  `card_no_back_url` varchar(100) DEFAULT NULL COMMENT '身份证反面',
  `tally_area_nos` varchar(200) DEFAULT NULL,
  `business_license_url` varchar(100) DEFAULT NULL COMMENT '营业执照',
  `sales_city_id` bigint(20) DEFAULT NULL,
  `sales_city_name` varchar(20) DEFAULT NULL,
  `state` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:启用 0：禁用',
  `password` varchar(50) NOT NULL,
  `yn` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 -1：删除',
  `version` tinyint(4) NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` bigint(20) DEFAULT '0' COMMENT '是否删除',
  `qr_status` int(11) DEFAULT '0' COMMENT '二维码状态(默认红色)',
  `user_type` tinyint(4) DEFAULT NULL COMMENT '用户类型 个人 10 企业 20',
  `market_id` bigint(20) DEFAULT NULL COMMENT '所属市场',
  `license` varchar(50) DEFAULT NULL COMMENT '统一信用代码',
  `legal_person` varchar(50) DEFAULT NULL COMMENT '法人姓名',
  `manufacturing_license_url` varchar(100) DEFAULT NULL COMMENT '生产许可证',
  `operation_license_url` varchar(100) DEFAULT NULL COMMENT '经营许可证',
  `vocation_type` tinyint(2) DEFAULT NULL COMMENT '批发 10 农贸 20 团体 30 个人40 餐饮50 配送商 60',
  `validate_state` tinyint(2) NOT NULL DEFAULT '10' COMMENT '未实名10 待审核 20  审核未通过 30 审核通过 40',
  `market_name` varchar(100) DEFAULT NULL,
  `business_category_ids` varchar(100) DEFAULT NULL COMMENT '经营品类',
  `business_categories` varchar(200) DEFAULT NULL COMMENT '经营品类',
  `source` tinyint(2) DEFAULT NULL COMMENT '来源 下游企业：10，注册：20',
  `pre_qr_status` int(11) DEFAULT NULL COMMENT '前一次二维码值',
  `open_id` varchar(50) DEFAULT NULL COMMENT '微信openId',
  `confirm_date` datetime DEFAULT NULL COMMENT '提示微信绑定，确认不再弹出日期',
  `is_push` tinyint(1) DEFAULT '-1' COMMENT '上报标志位，1待上报，-1无需上报',
  `is_active` tinyint(1) DEFAULT '-1' COMMENT '-1去活跃/1活跃',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_unique_phone` (`phone`,`is_delete`)
) ENGINE=InnoDB AUTO_INCREMENT=1370 DEFAULT CHARSET=utf8 COMMENT='来源 下游企业：10，注册：20\r\nsource';

/*Table structure for table `user_login_history` */

DROP TABLE IF EXISTS `user_login_history`;

CREATE TABLE `user_login_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `user_name` varchar(36) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19534 DEFAULT CHARSET=utf8mb4;

/*Table structure for table `user_plate` */

DROP TABLE IF EXISTS `user_plate`;

CREATE TABLE `user_plate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plate` varchar(20) NOT NULL COMMENT '车牌号',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=236 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Table structure for table `user_qr_history` */

DROP TABLE IF EXISTS `user_qr_history`;

CREATE TABLE `user_qr_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户(商户)ID',
  `user_name` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL,
  `qr_status` int(11) NOT NULL COMMENT '二维码状态',
  `content` varchar(200) DEFAULT NULL COMMENT '二维码转换信息内容',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `bill_id` bigint(20) DEFAULT NULL COMMENT '报备单ID',
  `verify_status` int(11) NOT NULL DEFAULT '0' COMMENT '查验状态',
  `is_valid` int(11) NOT NULL DEFAULT '1' COMMENT '是否有效',
  `trade_request_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1439 DEFAULT CHARSET=utf8;

/*Table structure for table `user_store` */

DROP TABLE IF EXISTS `user_store`;

CREATE TABLE `user_store` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `store_name` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `INDEX_USER_ID` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2118 DEFAULT CHARSET=utf8;

/*Table structure for table `usual_address` */

DROP TABLE IF EXISTS `usual_address`;

CREATE TABLE `usual_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) NOT NULL COMMENT '地址类型',
  `address_id` bigint(20) NOT NULL COMMENT '地址id',
  `address` varchar(20) NOT NULL COMMENT '地址',
  `merged_address` varchar(200) NOT NULL COMMENT '地址全名',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `today_used_count` int(11) DEFAULT '0' COMMENT '当天使用数量统计',
  `preday_used_count` int(11) DEFAULT '0' COMMENT '前一天使用数量统计',
  `clear_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '清理当天使用数量时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `usual_address_unique` (`address_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


-- 初始化脚本
insert into `biz_number` ( `type`, `value`, `memo`, `version`, `modified`, `created`) values('register_bill','2020102900051','订单编码','2294','2019-07-29 20:17:14','2018-11-02 09:45:13');
insert into `biz_number` ( `type`, `value`, `memo`, `version`, `modified`, `created`) values('register_bill_sample_code','2020011900051','登记单采样编号','839','2019-09-17 18:01:56','2019-09-17 18:01:40');
insert into `code_generate` ( `type`, `segment`, `seq`, `created`, `modified`, `pattern`, `prefix`) values('SAMPLE_CODE','20200622','5','2020-05-26 14:23:58','2020-06-22 09:22:50','yyyyMMdd','c');
insert into `code_generate` ( `type`, `segment`, `seq`, `created`, `modified`, `pattern`, `prefix`) values('CHECKSHEET_CODE','20200526','0','2020-05-26 14:23:58','2020-05-26 14:23:58','yyyyMMdd','SGJC');
insert into `code_generate` ( `type`, `segment`, `seq`, `created`, `modified`, `pattern`, `prefix`) values('TRADE_REQUEST_CODE','2020102609','2','2020-07-09 10:29:01','2020-10-26 09:56:40','yyyyMMddHH','HZSY');


-- 品类
-- 不同市场初始化不同的品类
-- 以下为杭州水产的数据
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('1',NULL,'贝类','贝类','1','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('2',NULL,'淡水鱼','淡水鱼','1','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('3',NULL,'高档海鲜','高档海鲜','1','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('4',NULL,'海水鱼','海水鱼','1','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('5',NULL,'河鲜','河鲜','1','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('6',NULL,'虾类','虾类','1','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('7',NULL,'其他','其他','1','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('8','1','白蛤','贝类,白蛤','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('9','1','白玉贝','贝类,白玉贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('10','1','北极贝','贝类,北极贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('11','1','朝鲜蚌','贝类,朝鲜蚌','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('12','1','蛏子','贝类,蛏子','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('13','1','赤贝','贝类,赤贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('14','1','大角螺','贝类,大角螺','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('15','1','淡菜','贝类,淡菜','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('16','1','刀贝','贝类,刀贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('17','1','钉螺','贝类,钉螺','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('18','1','肥贝','贝类,肥贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('19','1','广州蚌仔','贝类,广州蚌仔','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('20','1','蛤蜊','贝类,蛤蜊','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('21','1','海蚌','贝类,海蚌','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('22','1','海带花','贝类,海带花','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('23','1','海瓜子','贝类,海瓜子','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('24','1','海花','贝类,海花','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('25','1','海螺','贝类,海螺','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('26','1','河蚌','贝类,河蚌','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('27','1','红心蛏','贝类,红心蛏','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('28','1','花蛤','贝类,花蛤','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('29','1','花螺','贝类,花螺','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('30','1','黄贝','贝类,黄贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('31','1','黄金贝','贝类,黄金贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('32','1','黄金螺','贝类,黄金螺','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('33','1','黄螺','贝类,黄螺','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('34','1','扣贝','贝类,扣贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('35','1','辣螺','贝类,辣螺','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('36','1','老头蛤','贝类,老头蛤','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('37','1','龙须草','贝类,龙须草','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('38','1','螺蛳','贝类,螺蛳','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('39','1','芒果贝','贝类,芒果贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('40','1','毛贝','贝类,毛贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('41','1','美国贝','贝类,美国贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('42','1','牡蛎','贝类,牡蛎','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('43','1','泥螺','贝类,泥螺','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('44','1','鸟贝','贝类,鸟贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('45','1','七彩贝','贝类,七彩贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('46','1','青蛤','贝类,青蛤','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('47','1','青口贝','贝类,青口贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('48','1','扇贝','贝类,扇贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('49','1','生蚝','贝类,生蚝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('50','1','湿海蛎','贝类,湿海蛎','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('51','1','石头贝','贝类,石头贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('52','1','石蟹','贝类,石蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('53','1','特大贝','贝类,特大贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('54','1','天鹅蛋','贝类,天鹅蛋','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('55','1','田螺','贝类,田螺','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('56','1','文蛤','贝类,文蛤','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('57','1','蜗牛','贝类,蜗牛','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('58','1','西施贝','贝类,西施贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('59','1','香螺','贝类,香螺','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('60','1','小蚌','贝类,小蚌','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('61','1','血蛤','贝类,血蛤','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('62','1','一点红','贝类,一点红','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('63','1','油蛤','贝类,油蛤','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('64','1','月亮贝','贝类,月亮贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('65','1','珍珠贝','贝类,珍珠贝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('66','1','芝麻螺','贝类,芝麻螺','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('67','1','竹蛏','贝类,竹蛏','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('68','1','象拔蚌','贝类,象拔蚌','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('69','1','鲍鱼','贝类,鲍鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('70','2','草鱼','淡水鱼,草鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('71','2','花鲢','淡水鱼,花鲢','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('72','2','白莲','淡水鱼,白莲','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('73','2','鲫鱼','淡水鱼,鲫鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('74','2','鳊鱼','淡水鱼,鳊鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('75','2','黑鱼','淡水鱼,黑鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('76','2','鲤鱼','淡水鱼,鲤鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('77','2','青鱼','淡水鱼,青鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('78','2','湘鲫','淡水鱼,湘鲫','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('79','2','鲈鱼','淡水鱼,鲈鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('80','2','桂鱼','淡水鱼,桂鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('81','2','江刺鱼','淡水鱼,江刺鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('82','2','白条','淡水鱼,白条','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('83','2','罗非鱼','淡水鱼,罗非鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('84','2','河鳗','淡水鱼,河鳗','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('85','2','江鳗','淡水鱼,江鳗','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('86','2','鲶鱼','淡水鱼,鲶鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('87','2','红鮰','淡水鱼,红鮰','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('88','2','黑鮰','淡水鱼,黑鮰','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('89','2','洋花鱼','淡水鱼,洋花鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('90','2','鲻鱼','淡水鱼,鲻鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('91','2','太阳鱼','淡水鱼,太阳鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('92','2','鳕鱼','淡水鱼,鳕鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('93','2','牛尾鱼','淡水鱼,牛尾鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('94','2','中华鲟','淡水鱼,中华鲟','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('95','2','鱼钩鱼','淡水鱼,鱼钩鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('96','2','鸭嘴鱼','淡水鱼,鸭嘴鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('97','3','生态黄鱼','高档海鲜,生态黄鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('98','3','十八梅','高档海鲜,十八梅','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('99','3','美国红鱼','高档海鲜,美国红鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('100','3','金鲳鱼','高档海鲜,金鲳鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('101','3','黑珍鲷','高档海鲜,黑珍鲷','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('102','3','青斑','高档海鲜,青斑','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('103','3','石斑','高档海鲜,石斑','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('104','3','龙纹斑','高档海鲜,龙纹斑','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('105','3','老虎斑','高档海鲜,老虎斑','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('106','3','中华鲟','高档海鲜,中华鲟','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('107','3','淡水小石斑','高档海鲜,淡水小石斑','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('108','3','笋壳鱼','高档海鲜,笋壳鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('109','3','东星斑','高档海鲜,东星斑','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('110','3','多宝鱼','高档海鲜,多宝鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('111','3','左口鱼','高档海鲜,左口鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('112','3','鲍鱼','高档海鲜,鲍鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('113','3','澳鲍','高档海鲜,澳鲍','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('114','3','雪鱼','高档海鲜,雪鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('115','3','三文鱼','高档海鲜,三文鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('116','3','金枪鱼','高档海鲜,金枪鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('117','3','海鲈鱼','高档海鲜,海鲈鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('118','3','黄杉鱼','高档海鲜,黄杉鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('119','3','柳根鱼','高档海鲜,柳根鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('120','3','香鱼','高档海鲜,香鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('121','3','老鼠斑','高档海鲜,老鼠斑','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('122','3','河豚','高档海鲜,河豚','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('123','3','澳龙','高档海鲜,澳龙','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('124','3','红龙','高档海鲜,红龙','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('125','3','波士顿龙','高档海鲜,波士顿龙','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('126','3','小青龙','高档海鲜,小青龙','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('127','3','火龙','高档海鲜,火龙','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('128','3','南非龙','高档海鲜,南非龙','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('129','3','象拔蚌','高档海鲜,象拔蚌','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('130','3','海参','高档海鲜,海参','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('131','3','珍宝蟹','高档海鲜,珍宝蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('132','3','面包蟹','高档海鲜,面包蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('133','3','帝王蟹','高档海鲜,帝王蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('134','3','皇帝蟹','高档海鲜,皇帝蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('135','3','雪蟹','高档海鲜,雪蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('136','3','长脚蟹','高档海鲜,长脚蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('137','3','红花蟹','高档海鲜,红花蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('138','3','兰花蟹','高档海鲜,兰花蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('139','3','梭子蟹','高档海鲜,梭子蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('140','3','石蟹','高档海鲜,石蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('141','3','沙蟹','高档海鲜,沙蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('142','3','蓝蟹','高档海鲜,蓝蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('143','3','红毛蟹','高档海鲜,红毛蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('144','3','青蟹','高档海鲜,青蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('145','3','椰子蟹','高档海鲜,椰子蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('146','3','老虎蟹','高档海鲜,老虎蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('147','4','米鱼','海水鱼,米鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('148','4','龙头鱼','海水鱼,龙头鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('149','4','马面鱼','海水鱼,马面鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('150','4','龙利鱼','海水鱼,龙利鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('151','4','月亮鱼','海水鱼,月亮鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('152','4','刀鱼','海水鱼,刀鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('153','4','鲅鱼','海水鱼,鲅鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('154','4','青占鱼','海水鱼,青占鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('155','4','安康鱼','海水鱼,安康鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('156','4','墨鱼','海水鱼,墨鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('157','4','鱿鱼','海水鱼,鱿鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('158','4','章鱼','海水鱼,章鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('159','4','马头鱼','海水鱼,马头鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('160','4','海鳗','海水鱼,海鳗','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('161','4','海虾','海水鱼,海虾','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('162','4','带鱼','海水鱼,带鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('163','4','鲳鱼','海水鱼,鲳鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('164','4','鳓鱼','海水鱼,鳓鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('165','4','鲥鱼','海水鱼,鲥鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('166','4','黄花鱼','海水鱼,黄花鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('167','4','黄鲷鱼','海水鱼,黄鲷鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('168','4','黄菇鱼','海水鱼,黄菇鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('169','4','银鳕鱼','海水鱼,银鳕鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('170','4','鲽鱼','海水鱼,鲽鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('171','4','石斑鱼','海水鱼,石斑鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('172','4','金枪鱼','海水鱼,金枪鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('173','4','海鲈鱼','海水鱼,海鲈鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('174','4','秋刀鱼','海水鱼,秋刀鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('175','4','长寿鱼','海水鱼,长寿鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('176','4','虎头鱼','海水鱼,虎头鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('177','4','白菇鱼','海水鱼,白菇鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('178','4','梅童鱼','海水鱼,梅童鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('179','4','八爪鱼','海水鱼,八爪鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('180','5','黄鳝','河鲜,黄鳝','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('181','5','小龙虾','河鲜,小龙虾','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('182','5','甲鱼','河鲜,甲鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('183','5','泥鳅','河鲜,泥鳅','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('184','5','牛蛙','河鲜,牛蛙','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('185','5','江鳗','河鲜,江鳗','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('186','5','河鳗','河鲜,河鳗','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('187','5','白鳗','河鲜,白鳗','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('188','5','巴西龟','河鲜,巴西龟','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('189','5','草龟','河鲜,草龟','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('190','5','鳄鱼龟','河鲜,鳄鱼龟','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('191','5','湖蟹','河鲜,湖蟹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('192','6','黑虎虾','虾类,黑虎虾','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('193','6','白虾','虾类,白虾','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('194','6','南美白对虾','虾类,南美白对虾','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('195','6','基围虾','虾类,基围虾','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('196','6','河虾','虾类,河虾','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('197','6','沼虾','虾类,沼虾','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('198','6','竹节虾','虾类,竹节虾','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('199','6','富贵虾','虾类,富贵虾','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('200','6','牡丹虾','虾类,牡丹虾','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('201','6','青虾','虾类,青虾','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('202','6','明虾','虾类,明虾','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('203','6','草虾','虾类,草虾','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('204','7','鱼肚','其他,鱼肚','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('205','7','八爪鱼','其他,八爪鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('206','7','鱿鱼','其他,鱿鱼','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('207','7','蚕蛹','其他,蚕蛹','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('208','7','叽咕','其他,叽咕','2','2020-06-30 14:25:03','2020-06-30 14:25:03');
insert into `category` (`id`, `parent_id`, `name`, `full_name`, `level`, `created`, `modified`) values('209','7','海参','其他,海参','2','2020-06-30 14:25:03','2020-06-30 14:25:03');

-- 发送短信的数据配置初始化
insert  into `message_config`(`id`,`operation`,`message_flag`,`sms_flag`,`wechat_flag`,`sms_scene_code`,`wechat_template_id`,`event_message_title`,`event_message_content`) values
(1,10,'1','0','0','','','商户审核提醒','{0}，提交资料审核申请，请及时审核'),
(2,11,'0','0','0','passRegistered','','',''),
(3,12,'0','0','0','failedRegistration','','',''),
(4,20,'1','0','0','','','进场报备提醒','{0}，提交进场报备申请，请及时审核。'),
(5,21,'0','0','0','passReport','','',''),
(6,22,'0','0','0','failedReport','','',''),
(7,23,'0','0','0','returnReport','','',''),
(9,40,'1','0','0','','','订单交易成功','订单编号:{0}完成交易'),
(10,50,'0','0','0','tradeOrder','','',''),
(11,51,'0','0','0','tradeConfirm','','卖家交易订单测试消息',''),
(12,30,'1','0','0','','','场内报备提醒','{0}，提交场内报备申请，请及时审核。');

-- 微信小程序初始化脚本
-- 如果新申请的小程序，需要将app_id，app_secret改成最新小程序的
insert into applets_config (app_name,app_id,app_secret,access_token) values('溯源小程序','wx6802ffc21bf5cb01','8739293c3d9d51f224f20bbce52290ae','init');

-- 初始化全局配置
insert into `sys_config` (`id`, `instructions`, `opt_type`, `opt_category`, `opt_value`) values('1','展示大屏用户基数设定','statisticBaseUser','trade','5');
insert into `sys_config` (`id`, `instructions`, `opt_type`, `opt_category`, `opt_value`) values('2','展示大屏用户基数设定','statisticBaseUser','bill','5');
insert into `sys_config` (`id`, `instructions`, `opt_type`, `opt_category`, `opt_value`) values('4','用户活跃度时效天数','operation_report_limit_day','operation_report_limit_day','30');
