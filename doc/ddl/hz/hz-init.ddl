

USE `hztrace`;

--
-- Table structure for table `approver_info`
--

DROP TABLE IF EXISTS `approver_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `approver_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) NOT NULL COMMENT '审核人名字',
  `user_id` bigint(20) NOT NULL COMMENT '审核人ID',
  `phone` varchar(20) DEFAULT NULL COMMENT '审核人电话',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base64_signature`
--

DROP TABLE IF EXISTS `base64_signature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base64_signature` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `approver_info_id` bigint(20) NOT NULL COMMENT '审核人ID',
  `base64` varchar(1000) NOT NULL COMMENT '审核人签名Base64图片',
  `order_num` int(11) NOT NULL COMMENT '顺序',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3733 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biz_number`
--

DROP TABLE IF EXISTS `biz_number`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `check_sheet`
--

DROP TABLE IF EXISTS `check_sheet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `check_sheet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(30) NOT NULL COMMENT '编号',
  `id_card_no` varchar(20) NOT NULL COMMENT '提交人身份证号',
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=445 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `check_sheet_detail`
--

DROP TABLE IF EXISTS `check_sheet_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `check_sheet_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `check_sheet_id` bigint(20) NOT NULL COMMENT '检测报告单ID',
  `register_bill_id` bigint(20) NOT NULL COMMENT '登记单ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `product_name` varchar(20) NOT NULL COMMENT '商品名称',
  `product_alias_name` varchar(20) DEFAULT NULL COMMENT '商品别名',
  `origin_id` bigint(20) NOT NULL COMMENT '产地ID',
  `origin_name` varchar(20) NOT NULL COMMENT '产地',
  `order_number` int(11) NOT NULL COMMENT '序号',
  `detect_state` int(11) NOT NULL COMMENT '检测状态',
  `latest_pd_result` varchar(100) DEFAULT NULL COMMENT '检测结果',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=480 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `code_generate`
--

DROP TABLE IF EXISTS `code_generate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `detect_record`
--

DROP TABLE IF EXISTS `detect_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=196513 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quality_trace_trade_bill`
--

DROP TABLE IF EXISTS `quality_trace_trade_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `match_status` int(11) DEFAULT NULL COMMENT '匹配状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `BILL_ID_UNIQUE_INDEX` (`bill_id`),
  KEY `ORDER_ID_INDEX` (`order_id`) USING BTREE,
  KEY `REGISTER_BILL_CODE_INDEX` (`register_bill_code`) USING BTREE,
  KEY `ix_match_st` (`match_status`)
) ENGINE=InnoDB AUTO_INCREMENT=1095490 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quality_trace_trade_bill_syncpoint`
--

DROP TABLE IF EXISTS `quality_trace_trade_bill_syncpoint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quality_trace_trade_bill_syncpoint` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) DEFAULT NULL COMMENT '流水号',
  `order_id` varchar(50) NOT NULL COMMENT '订单号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1095490 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `register_bill`
--

DROP TABLE IF EXISTS `register_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `register_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL COMMENT '编号',
  `sample_code` varchar(20) DEFAULT NULL COMMENT '采样编号',
  `register_source` tinyint(4) NOT NULL COMMENT '1.理货区 2.交易区',
  `tally_area_no` varchar(60) DEFAULT NULL COMMENT '理货区号',
  `name` varchar(50) DEFAULT NULL COMMENT '业户姓名',
  `id_card_no` varchar(20) DEFAULT NULL COMMENT '身份证号',
  `addr` varchar(50) DEFAULT NULL COMMENT '地址',
  `phone` varchar(20) DEFAULT NULL,
  `trade_account` varchar(20) DEFAULT NULL COMMENT '交易区账号',
  `trade_printing_card` varchar(50) DEFAULT NULL,
  `trade_type_id` varchar(10) DEFAULT NULL,
  `trade_type_name` varchar(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL COMMENT '理货区用户ID',
  `plate` varchar(15) NOT NULL COMMENT '车牌',
  `state` tinyint(1) NOT NULL COMMENT '1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中，8.审核未通过',
  `sales_type` tinyint(1) DEFAULT NULL COMMENT '1.分销 2.全销',
  `detect_report_url` varchar(1000) DEFAULT NULL,
  `product_name` varchar(20) NOT NULL COMMENT '商品名称',
  `product_id` bigint(20) NOT NULL,
  `origin_certifiy_url` varchar(1000) DEFAULT NULL,
  `origin_id` bigint(20) DEFAULT NULL COMMENT '产地ID',
  `origin_name` varchar(50) DEFAULT NULL COMMENT '产地',
  `weight` int(11) DEFAULT NULL COMMENT '重量',
  `operator_id` bigint(20) DEFAULT NULL,
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人',
  `sample_source` tinyint(1) DEFAULT NULL COMMENT '1:采样检测 2:主动送检',
  `detect_state` tinyint(1) DEFAULT NULL COMMENT '默认null,1.合格 2.不合格 3.复检合格 4.复检不合格',
  `latest_detect_record_id` bigint(20) DEFAULT NULL COMMENT '检测记录ID',
  `latest_detect_operator` varchar(20) DEFAULT NULL COMMENT '检测人员',
  `latest_detect_time` timestamp NULL DEFAULT NULL COMMENT '检测时间',
  `latest_pd_result` varchar(100) DEFAULT NULL COMMENT '最新一次检测值',
  `exe_machine_no` varchar(20) DEFAULT NULL COMMENT '检测仪器码',
  `handle_result_url` varchar(1000) DEFAULT NULL,
  `handle_result` varchar(4000) DEFAULT NULL,
  `version` tinyint(4) NOT NULL DEFAULT '0' COMMENT '版本号',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `creation_source` int(11) DEFAULT '10' COMMENT '数据创建来源',
  `check_sheet_id` bigint(20) DEFAULT NULL COMMENT '检测报告单ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `register_bill_unique_code` (`code`),
  KEY `ix_created` (`created`,`product_name`)
) ENGINE=InnoDB AUTO_INCREMENT=230899 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `separate_sales_record`
--

DROP TABLE IF EXISTS `separate_sales_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `separate_sales_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sales_city_id` bigint(20) NOT NULL COMMENT '分销城市',
  `sales_city_name` varchar(20) NOT NULL COMMENT '分销城市',
  `sales_user_id` bigint(20) DEFAULT NULL,
  `sales_user_name` varchar(20) NOT NULL COMMENT '分销人',
  `sales_plate` varchar(15) DEFAULT NULL COMMENT '分销车牌号',
  `sales_weight` int(11) NOT NULL COMMENT '分销重量KG',
  `register_bill_code` varchar(20) DEFAULT NULL COMMENT '被分销的登记单编码',
  `trade_no` varchar(20) DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `REGISTER_BILL_CODE_INDEX` (`register_bill_code`) USING BTREE,
  KEY `TRADE_NO_INDEX` (`trade_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `card_no` varchar(20) NOT NULL,
  `addr` varchar(50) NOT NULL,
  `card_no_front_url` varchar(100) DEFAULT NULL COMMENT '身份证正面',
  `card_no_back_url` varchar(100) DEFAULT NULL COMMENT '身份证反面',
  `tally_area_nos` varchar(60) DEFAULT NULL,
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_tailly_area_no` (`tally_area_nos`),
  UNIQUE KEY `user_unique_phone` (`phone`,`is_delete`),
  UNIQUE KEY `user_unique_cardno` (`card_no`,`is_delete`)
) ENGINE=InnoDB AUTO_INCREMENT=450 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_history`
--

DROP TABLE IF EXISTS `user_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `yn` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 -1：删除',
  `user_plates` varchar(800) DEFAULT NULL COMMENT '车牌',
  `plate_amount` int(11) NOT NULL DEFAULT '0' COMMENT '车牌数量',
  `version` tinyint(4) NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=627 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_plate`
--

DROP TABLE IF EXISTS `user_plate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_plate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plate` varchar(20) NOT NULL COMMENT '车牌号',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `TALLY_AREA_NO_UNIQUE_INDEX` (`plate`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1539 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_tally_area`
--

DROP TABLE IF EXISTS `user_tally_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_tally_area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tally_area_no` varchar(20) NOT NULL COMMENT '理货区号',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `TALLY_AREA_NO_UNIQUE_INDEX` (`tally_area_no`)
) ENGINE=InnoDB AUTO_INCREMENT=2283 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usual_address`
--

DROP TABLE IF EXISTS `usual_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;

