-- MySQL dump 10.13  Distrib 5.5.62, for Win64 (AMD64)
--
-- Host: 10.35.100.34    Database: dili_trace
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `trade_order`
--

DROP TABLE IF EXISTS `trade_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trade_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_status` int NOT NULL COMMENT '订单状态',
  `order_type` int NOT NULL COMMENT '订单类型',
  `buyer_id` bigint DEFAULT NULL COMMENT '买家ID',
  `buyer_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `seller_id` bigint DEFAULT NULL COMMENT '卖家ID',
  `seller_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `buyer_market_id` bigint DEFAULT '1' COMMENT '买家市场ID',
  `seller_market_id` bigint DEFAULT '1' COMMENT '卖家市场ID',
  `buyer_type` int NOT NULL DEFAULT '1' COMMENT '买家类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purchase_intention_record`
--

DROP TABLE IF EXISTS `purchase_intention_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase_intention_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `buyer_name` varchar(50) DEFAULT NULL COMMENT '买家姓名',
  `buyer_id` bigint NOT NULL COMMENT '买家ID',
  `product_name` varchar(20) DEFAULT NULL COMMENT '商品名称',
  `product_id` bigint DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `operator_name` varchar(50) DEFAULT NULL,
  `operator_id` bigint DEFAULT NULL,
  `market_id` bigint DEFAULT NULL COMMENT '市场id',
  `buyer_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '买家电话',
  `weight_unit` int DEFAULT NULL COMMENT '重量单位',
  `product_weight` decimal(11,3) NOT NULL DEFAULT '0.000' COMMENT '商品重量',
  `plate` varchar(10) DEFAULT NULL COMMENT '车牌',
  `code` varchar(20) NOT NULL COMMENT '买家报备编号',
  `corporate_name` varchar(100) DEFAULT NULL COMMENT '企业名称',
  `brand_id` bigint DEFAULT NULL COMMENT '品牌ID',
  `brand_name` varchar(50) DEFAULT NULL COMMENT '品牌名称',
  `origin_id` bigint DEFAULT NULL COMMENT '产地ID',
  `origin_name` varchar(50) DEFAULT NULL COMMENT '产地',
  `state` int DEFAULT '1' COMMENT '1启用/2禁用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='买家进货意向记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `detect_request`
--

DROP TABLE IF EXISTS `detect_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `detect_request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bill_id` bigint NOT NULL COMMENT '报备单ID',
  `creator_id` bigint DEFAULT NULL COMMENT '创建人ID',
  `creator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '创建人姓名',
  `designated_id` bigint DEFAULT NULL COMMENT '指定检测员ID',
  `designated_name` varchar(50) DEFAULT NULL COMMENT '指定检测员姓名',
  `detect_type` int NOT NULL COMMENT '检测类型',
  `created` datetime NOT NULL COMMENT '创建时间（预约时间）',
  `modified` datetime NOT NULL,
  `detector_id` bigint DEFAULT NULL COMMENT '检测员ID',
  `detector_name` varchar(50) DEFAULT NULL COMMENT '检测员姓名',
  `detect_result` int NOT NULL COMMENT '检测结果',
  `detect_source` int NOT NULL COMMENT '采样来源',
  `detect_time` datetime DEFAULT NULL COMMENT '检测时间',
  `detect_fee` decimal(10,3) DEFAULT '0.000' COMMENT '检测费用',
  `confirm_time` datetime DEFAULT NULL COMMENT '接单时间',
  `sample_time` datetime DEFAULT NULL COMMENT '采样时间',
  `detect_reservation_time` datetime DEFAULT NULL COMMENT '检测请求预约时间',
  `detect_code` varchar(20) DEFAULT NULL COMMENT '检测单编号',
  `scheduled_detect_time` datetime DEFAULT NULL COMMENT '检测指定时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=172 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `r_user_wechat`
--

DROP TABLE IF EXISTS `r_user_wechat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `r_user_wechat` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `open_id` varchar(50) CHARACTER SET latin1 DEFAULT NULL COMMENT '微信openid',
  `phone` varchar(15) CHARACTER SET latin1 DEFAULT NULL COMMENT '电话号码',
  `user_id` bigint DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(50) CHARACTER SET latin1 DEFAULT NULL COMMENT '用户名',
  `active` tinyint(1) DEFAULT NULL COMMENT '是否激活',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `r_user_upstream`
--

DROP TABLE IF EXISTS `r_user_upstream`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `r_user_upstream` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户(商户)ID',
  `upstream_id` bigint NOT NULL COMMENT '上游信息ID',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作人ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=238 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_login_history`
--

DROP TABLE IF EXISTS `user_login_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_login_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `user_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sms_message`
--

DROP TABLE IF EXISTS `sms_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sms_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `source_business_type` tinyint DEFAULT NULL COMMENT '关联业务类型 用户 10 报备 20 交易 30',
  `source_business_id` bigint DEFAULT NULL COMMENT '关联业务单据id',
  `receive_phone` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '接收短信手机号码',
  `send_reason` tinyint DEFAULT NULL,
  `result_code` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '短信发送返回结果码',
  `result_info` varchar(2000) CHARACTER SET utf8 DEFAULT NULL COMMENT '短信发送返回结果信息',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image_cert`
--

DROP TABLE IF EXISTS `image_cert`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image_cert` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bill_id` bigint DEFAULT NULL COMMENT '所属数据ID',
  `url` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '图片URL',
  `cert_type` int NOT NULL COMMENT '图片类型',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `bill_type` tinyint DEFAULT '1' COMMENT '单据类型。1-报备单 2-检测单 3-检测不合格处置单 4-进门主台账单。默认为1',
  `uid` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '图片唯一id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2055 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `third_party_report_data`
--

DROP TABLE IF EXISTS `third_party_report_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `third_party_report_data` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(500) DEFAULT NULL COMMENT '上报名称',
  `type` int DEFAULT NULL COMMENT '上报类型',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人姓名',
  `data` text COMMENT '提交数据结果',
  `success` int DEFAULT NULL COMMENT '是否成功执行',
  `msg` varchar(150) DEFAULT NULL COMMENT '执行结果',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `market_id` bigint DEFAULT '1' COMMENT '市场ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_store`
--

DROP TABLE IF EXISTS `user_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_store` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `store_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_name` varchar(50) DEFAULT NULL COMMENT '业务名',
  `market_id` bigint DEFAULT NULL COMMENT '市场ID',
  `market_name` varchar(100) DEFAULT NULL COMMENT '市场名称',
  PRIMARY KEY (`id`),
  KEY `INDEX_USER_ID` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_history`
--

DROP TABLE IF EXISTS `user_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT 'ID',
  `name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '名称',
  `phone` varchar(15) CHARACTER SET utf8 NOT NULL COMMENT '手机号',
  `card_no` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '身份证号',
  `addr` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '地址',
  `card_no_front_url` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '身份证正面',
  `card_no_back_url` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '身份证反面',
  `tally_area_nos` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '理货区号',
  `business_license_url` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '营业执照URL',
  `sales_city_id` bigint DEFAULT NULL COMMENT '销售城市ID',
  `sales_city_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '销售城市名称',
  `state` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:启用 0：禁用',
  `password` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '密码',
  `yn` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 -1：删除',
  `user_plates` varchar(800) CHARACTER SET utf8 DEFAULT NULL COMMENT '车牌',
  `plate_amount` int NOT NULL DEFAULT '0' COMMENT '车牌数量',
  `version` tinyint NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=431 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `third_party_push_data`
--

DROP TABLE IF EXISTS `third_party_push_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `third_party_push_data` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `interface_name` varchar(50) NOT NULL,
  `table_name` varchar(50) NOT NULL,
  `push_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `market_id` bigint DEFAULT '2' COMMENT '市场ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=175 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `base64_signature`
--

DROP TABLE IF EXISTS `base64_signature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `base64_signature` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `approver_info_id` bigint NOT NULL COMMENT '审核人ID',
  `base64` varchar(1000) CHARACTER SET utf8 NOT NULL COMMENT '审核人签名Base64图片',
  `order_num` int NOT NULL COMMENT '顺序',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2519 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `r_user_tally_area`
--

DROP TABLE IF EXISTS `r_user_tally_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `r_user_tally_area` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tally_area_no_id` bigint DEFAULT NULL COMMENT '摊位号ID',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_tally_area`
--

DROP TABLE IF EXISTS `user_tally_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_tally_area` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tally_area_no` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '理货区号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `TALLY_AREA_NO_UNIQUE_INDEX` (`tally_area_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=614 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trade_detail`
--

DROP TABLE IF EXISTS `trade_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trade_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint DEFAULT NULL COMMENT '分销来源ID',
  `bill_id` bigint NOT NULL COMMENT '登记单ID',
  `checkin_record_id` bigint DEFAULT NULL COMMENT '进门ID',
  `checkout_record_id` bigint DEFAULT NULL COMMENT '进门ID',
  `checkin_status` int NOT NULL COMMENT '进门状态',
  `checkout_status` int NOT NULL COMMENT '出门状态',
  `sale_status` int NOT NULL COMMENT '交易状态',
  `trade_type` int NOT NULL COMMENT '交易类型',
  `buyer_id` bigint NOT NULL COMMENT '买家ID',
  `buyer_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `seller_id` bigint DEFAULT NULL COMMENT '卖家ID',
  `seller_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `product_name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '商品名称',
  `stock_weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '库存重量',
  `total_weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '总重量',
  `weight_unit` int NOT NULL DEFAULT '10' COMMENT '重量单位',
  `product_stock_id` bigint DEFAULT NULL COMMENT '商品库存ID',
  `trade_request_id` bigint DEFAULT NULL COMMENT '交易请求ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_batched` int NOT NULL DEFAULT '0' COMMENT '是否计算入BatchStock',
  `batch_no` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '批次号',
  `parent_batch_no` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '父批次号',
  `pushaway_weight` decimal(10,3) DEFAULT '0.000',
  `soft_weight` decimal(10,3) DEFAULT '0.000',
  `third_party_stock_id` bigint DEFAULT NULL COMMENT '第三方库存主键',
  `buyer_type` int NOT NULL DEFAULT '1' COMMENT '买家类型',
  PRIMARY KEY (`id`),
  KEY `index_trade_detail_bill_id` (`checkin_record_id`),
  KEY `bill_id` (`bill_id`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bill_verify_history`
--

DROP TABLE IF EXISTS `bill_verify_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bill_verify_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `bill_id` bigint NOT NULL COMMENT '登记单ID',
  `verify_date_time` datetime DEFAULT NULL COMMENT '审核时间',
  `verify_operator_id` varchar(255) DEFAULT NULL COMMENT '审核人ID',
  `verify_operator_name` varchar(255) DEFAULT NULL COMMENT '审核人姓名',
  `previous_verify_status` varchar(255) DEFAULT NULL COMMENT '审核前状态值',
  `created` datetime DEFAULT NULL COMMENT '创建时间',
  `modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `register_head`
--

DROP TABLE IF EXISTS `register_head`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `register_head` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '表主键',
  `code` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '主台账编号',
  `bill_type` int NOT NULL COMMENT '单据类型。10-正常进场 20-补单 30-外冷分批进场。',
  `user_id` bigint NOT NULL COMMENT '业户ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业户姓名',
  `id_card_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业户身份证号',
  `third_party_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '经营户卡号',
  `addr` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '业户地址',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业户手机',
  `plate` varchar(15) CHARACTER SET utf8 DEFAULT NULL COMMENT '车牌号',
  `product_id` bigint NOT NULL COMMENT '商品id',
  `product_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '商品名称',
  `measure_type` tinyint NOT NULL COMMENT '计量类型。10-计件 20-计重。默认计件。',
  `piece_num` decimal(10,3) DEFAULT NULL COMMENT '件数',
  `piece_weight` decimal(11,3) DEFAULT NULL COMMENT '件重',
  `weight` decimal(14,3) NOT NULL DEFAULT '0.000' COMMENT '总重量',
  `remain_weight` decimal(11,3) NOT NULL DEFAULT '0.000' COMMENT '剩余重量',
  `weight_unit` int NOT NULL COMMENT '重量单位。1-斤 2-公斤。默认1。',
  `upstream_id` bigint NOT NULL COMMENT '上游id',
  `spec_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '规格',
  `origin_id` bigint DEFAULT NULL COMMENT '产地id',
  `origin_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '产地',
  `brand_id` bigint DEFAULT NULL COMMENT '品牌id',
  `brand_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '品牌名称',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_user` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_user` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '修改人',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint DEFAULT NULL COMMENT '是否作废。0-否 1-是',
  `delete_user` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '作废人',
  `delete_time` datetime DEFAULT NULL COMMENT '作废时间',
  `version` tinyint NOT NULL COMMENT '版本号',
  `reason` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '原因',
  `active` tinyint DEFAULT NULL COMMENT '是否启用。0-否 1-是',
  `market_id` bigint DEFAULT NULL COMMENT '市场ID',
  `truck_type` int DEFAULT NULL COMMENT '拼车类型',
  `unit_price` decimal(10,3) DEFAULT NULL COMMENT '单价',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `truck_enter_record`
--

DROP TABLE IF EXISTS `truck_enter_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `truck_enter_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(20) NOT NULL COMMENT '编号',
  `truck_plate` varchar(10) NOT NULL COMMENT '车牌',
  `truck_type_id` bigint NOT NULL COMMENT '车型ID',
  `truck_type_name` varchar(20) NOT NULL COMMENT '车型名称',
  `driver_id` bigint NOT NULL COMMENT '司机ID',
  `driver_name` varchar(50) NOT NULL COMMENT '司机姓名',
  `corporate_name` varchar(100) DEFAULT NULL COMMENT '企业名称',
  `created` datetime DEFAULT NULL,
  `modified` datetime DEFAULT NULL,
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人ID',
  `market_id` bigint DEFAULT NULL COMMENT '市场id',
  `driver_phone` varchar(20) DEFAULT NULL COMMENT '司机电话',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='车子进门记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tally_area_no`
--

DROP TABLE IF EXISTS `tally_area_no`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tally_area_no` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `number` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '摊位号',
  `street` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '街区号',
  `area` varchar(30) CHARACTER SET utf8 NOT NULL COMMENT '区域',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `market_id` bigint DEFAULT '1' COMMENT '市场ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `third_party_source_data`
--

DROP TABLE IF EXISTS `third_party_source_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `third_party_source_data` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(500) DEFAULT NULL COMMENT '来源名称',
  `type` int DEFAULT NULL COMMENT '类型',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人姓名',
  `data` longtext COMMENT '数据详情',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `report_flag` tinyint(1) DEFAULT '-1' COMMENT '上报标志位-1未处理/1待上报/2已上报',
  `market_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=308 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trade_push_log`
--

DROP TABLE IF EXISTS `trade_push_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trade_push_log` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `trade_detail_id` bigint DEFAULT NULL COMMENT '批次主键',
  `log_type` tinyint DEFAULT NULL COMMENT '0:下架 1:上架',
  `product_name` varchar(50) DEFAULT NULL COMMENT '商品名称',
  `operation_weight` decimal(10,3) DEFAULT NULL COMMENT '上下架重量',
  `order_type` tinyint DEFAULT NULL COMMENT '0：报备单 10：交易单',
  `order_id` bigint DEFAULT NULL COMMENT '单据主键id。报备单id或者交易单id',
  `user_id` bigint DEFAULT NULL COMMENT '商户 id',
  `product_stock_id` bigint DEFAULT NULL COMMENT '库存 id',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `operation_reason` varchar(200) DEFAULT NULL,
  `order_code` varchar(20) DEFAULT NULL,
  `market_id` bigint DEFAULT '2' COMMENT '市场ID',
  PRIMARY KEY (`id`),
  KEY `INDEX_TRADE_DETAIL_ID` (`user_id`,`trade_detail_id`),
  KEY `INDEX_USER_ID` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `check_sheet`
--

DROP TABLE IF EXISTS `check_sheet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `check_sheet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(30) CHARACTER SET utf8 NOT NULL COMMENT '编号',
  `id_card_no` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '提交人身份证号',
  `user_name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '提交人姓名',
  `valid_period` int NOT NULL COMMENT '有效天数',
  `detect_operator_id` bigint NOT NULL COMMENT '检测人ID',
  `detect_operator_name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '检测人姓名',
  `qrcode_url` varchar(150) CHARACTER SET utf8 NOT NULL COMMENT '二维码url',
  `approver_info_id` bigint NOT NULL COMMENT '审核人ID',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `operator_id` bigint NOT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '操作人姓名',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `bill_type` int DEFAULT NULL COMMENT '登记单类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `code_generate`
--

DROP TABLE IF EXISTS `code_generate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `code_generate` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编号类型',
  `segment` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '当前编号段',
  `seq` bigint DEFAULT NULL COMMENT '当前编号',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `pattern` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '模式',
  `prefix` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '前缀',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_plate`
--

DROP TABLE IF EXISTS `user_plate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_plate` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `plate` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '车牌号',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=114 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `check_sheet_detail`
--

DROP TABLE IF EXISTS `check_sheet_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `check_sheet_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `check_sheet_id` bigint NOT NULL COMMENT '检测报告单ID',
  `product_name` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '商品名称',
  `product_alias_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '商品别名',
  `origin_name` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '产地',
  `order_number` int NOT NULL COMMENT '序号',
  `latest_pd_result` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '检测结果',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `register_bill_id` bigint DEFAULT NULL,
  `verify_status` int DEFAULT NULL COMMENT '审核状态',
  `detect_status` int DEFAULT NULL COMMENT '检测状态',
  `detect_result` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `message_config`
--

DROP TABLE IF EXISTS `message_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `operation` tinyint DEFAULT NULL COMMENT '10:商户注册提交;11:商户注册审核通过;12:商户注册审核未通过;20:提交报备;21:报备审核通过;22:报备审核未通过;23:报备审核退回;30:进门审核;40:卖家下单;50:买家下单;51:卖家确认订单',
  `message_flag` char(1) CHARACTER SET utf8 DEFAULT NULL COMMENT '是否发站内信',
  `sms_flag` char(1) CHARACTER SET utf8 DEFAULT NULL COMMENT '是否发短信',
  `wechat_flag` char(1) CHARACTER SET utf8 DEFAULT NULL COMMENT '是否发微信',
  `sms_scene_code` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '短信中心sceneCode',
  `wechat_template_id` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '微信模板id',
  `event_message_title` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '站内消息标题',
  `event_message_content` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '站内消息内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quality_trace_trade_bill`
--

DROP TABLE IF EXISTS `quality_trace_trade_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quality_trace_trade_bill` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bill_id` bigint NOT NULL COMMENT '流水号',
  `register_bill_code` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `sales_type` tinyint(1) DEFAULT NULL COMMENT '1.分销 2.全销',
  `order_id` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '订单号',
  `order_status` tinyint(1) DEFAULT NULL,
  `seller_account` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '卖家账号',
  `seller_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '卖家名称',
  `sellerIDNo` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `buyer_account` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '买家账号',
  `buyer_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '买家名称',
  `buyerIDNo` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `order_create_date` datetime DEFAULT NULL COMMENT '订单创建时间',
  `order_pay_date` datetime DEFAULT NULL COMMENT '订单支付时间',
  `pdResult` decimal(18,2) DEFAULT NULL COMMENT '残留值',
  `conclusion` tinyint(1) DEFAULT NULL COMMENT '合格值  0-未知 1合格  2不合格 3作废',
  `check_result_EID` bigint DEFAULT NULL COMMENT '检测结算单唯一标志,NULL表示无检测信息',
  `trade_flow_id` bigint DEFAULT NULL COMMENT '交易号',
  `total_money` bigint DEFAULT NULL COMMENT '总金额',
  `order_item_id` bigint DEFAULT NULL COMMENT '订单项号',
  `product_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '商品名称',
  `cate_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '品类名称',
  `price` bigint DEFAULT NULL COMMENT '单价(分)',
  `piece_quantity` bigint DEFAULT NULL COMMENT '件数',
  `piece_weight` bigint DEFAULT NULL COMMENT '件重(公斤)',
  `net_weight` bigint DEFAULT NULL COMMENT '总净重(公斤)',
  `tradetype_id` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '交易类型编码',
  `tradetype_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '交易类型名称',
  `bill_active` int DEFAULT NULL COMMENT '状态',
  `sale_unit` int DEFAULT NULL COMMENT '销售单位 1:斤 2：件',
  `match_status` int DEFAULT NULL COMMENT '匹配状态',
  `order_version` int NOT NULL DEFAULT '1' COMMENT '订单版本',
  PRIMARY KEY (`id`),
  UNIQUE KEY `BILL_ID_UNIQUE_INDEX` (`bill_id`) USING BTREE,
  KEY `ORDER_ID_INDEX` (`order_id`) USING BTREE,
  KEY `REGISTER_BILL_CODE_INDEX` (`register_bill_code`) USING BTREE,
  KEY `ix_match_st` (`match_status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=322227 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usual_address`
--

DROP TABLE IF EXISTS `usual_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usual_address` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '地址类型',
  `address_id` bigint NOT NULL COMMENT '地址id',
  `address` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '地址',
  `merged_address` varchar(200) CHARACTER SET utf8 NOT NULL COMMENT '地址全名',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `today_used_count` int DEFAULT '0' COMMENT '当天使用数量统计',
  `preday_used_count` int DEFAULT '0' COMMENT '前一天使用数量统计',
  `clear_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '清理当天使用数量时间',
  `market_id` bigint DEFAULT NULL COMMENT '市场id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `usual_address_unique` (`address_id`,`type`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `check_data`
--

DROP TABLE IF EXISTS `check_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `check_data` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `check_id` bigint NOT NULL,
  `project` varchar(100) DEFAULT NULL COMMENT '检测项名称',
  `normal_value` varchar(200) DEFAULT NULL COMMENT '检测标准值',
  `result` varchar(50) DEFAULT NULL COMMENT '检测结果',
  `value` varchar(50) DEFAULT NULL COMMENT '检测数据值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_ext`
--

DROP TABLE IF EXISTS `user_ext`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_ext` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '经营户id',
  `credential_type` varchar(10) DEFAULT NULL COMMENT '证件类型',
  `credential_name` varchar(20) DEFAULT NULL COMMENT '证件名称',
  `credential_number` varchar(30) DEFAULT NULL COMMENT '证件号码',
  `credential_url` varchar(100) DEFAULT NULL COMMENT '证件图片地址',
  `id_addr` varchar(50) DEFAULT NULL COMMENT '身份证地址',
  `whereis` varchar(50) DEFAULT NULL COMMENT '商品去向',
  `credit_limit` varchar(11) DEFAULT NULL COMMENT '授信额度',
  `effective_date` timestamp NULL DEFAULT NULL COMMENT '卡有效期',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `sex` varchar(2) DEFAULT NULL COMMENT '性别',
  `fixed_telephone` varchar(20) DEFAULT NULL COMMENT '固定电话',
  `charge_rate` decimal(5,2) DEFAULT '0.00' COMMENT '手续费折扣率',
  `manger_rate` decimal(5,2) DEFAULT '0.00' COMMENT '包装管理费折扣率',
  `storage_rate` decimal(5,2) DEFAULT '0.00' COMMENT '仓储费折扣率',
  `assess_rate` decimal(5,2) DEFAULT '0.00' COMMENT '员工考核折扣率',
  `approver` varchar(10) DEFAULT NULL COMMENT '折扣率批准人',
  `supplier_type` varchar(10) DEFAULT NULL COMMENT '供应商类型（大客户、临时客户）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `register_bill_history`
--

DROP TABLE IF EXISTS `register_bill_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `register_bill_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bill_id` bigint NOT NULL,
  `code` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '编号',
  `sample_code` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '采样编号',
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '业户姓名',
  `id_card_no` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '身份证号',
  `addr` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '地址',
  `phone` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `user_id` bigint DEFAULT NULL COMMENT '理货区用户ID',
  `plate` varchar(15) CHARACTER SET utf8 DEFAULT NULL COMMENT '车牌',
  `state` tinyint(1) DEFAULT NULL COMMENT '1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中，8.审核未通过',
  `sales_type` tinyint(1) DEFAULT NULL COMMENT '1.分销 2.全销',
  `product_name` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '商品名称',
  `product_id` bigint NOT NULL,
  `origin_id` bigint DEFAULT NULL COMMENT '产地ID',
  `origin_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '产地',
  `weight` decimal(15,3) NOT NULL DEFAULT '0.000' COMMENT '重量',
  `operator_id` bigint DEFAULT NULL,
  `operator_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作人ID',
  `sample_source` tinyint(1) DEFAULT NULL COMMENT '1:采样检测 2:主动送检',
  `detect_state` tinyint(1) DEFAULT NULL COMMENT '默认null,1.合格 2.不合格 3.复检合格 4.复检不合格',
  `latest_detect_record_id` bigint DEFAULT NULL COMMENT '检测记录ID',
  `latest_detect_operator` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '检测人员',
  `latest_detect_time` timestamp NULL DEFAULT NULL COMMENT '检测时间',
  `latest_pd_result` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最新一次检测值',
  `version` tinyint NOT NULL DEFAULT '0' COMMENT '版本号',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `upstream_id` bigint DEFAULT NULL COMMENT '上游信息ID',
  `complete` int DEFAULT NULL COMMENT '信息是否完整',
  `verify_status` int NOT NULL DEFAULT '0' COMMENT '查验状态',
  `weight_unit` int NOT NULL DEFAULT '1' COMMENT '重量单位',
  `preserve_type` int NOT NULL DEFAULT '10' COMMENT '保存类型',
  `verify_type` int NOT NULL DEFAULT '0' COMMENT '查验类型',
  `spec_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '规格',
  `bill_type` int NOT NULL DEFAULT '10' COMMENT '报备类型',
  `brand_id` bigint DEFAULT NULL COMMENT '品牌ID',
  `brand_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '品牌名称',
  `truck_type` int NOT NULL DEFAULT '10' COMMENT '车类型',
  `tally_area_no` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '摊位号',
  `reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '原因',
  `is_checkin` int NOT NULL DEFAULT '-1' COMMENT '是否进门',
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否被删除',
  `operation_time` datetime DEFAULT NULL COMMENT '操作时间',
  `verify_operator_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '审核操作人姓名',
  `verify_operator_id` bigint DEFAULT NULL COMMENT '审核操作人ID',
  `market_id` bigint DEFAULT NULL COMMENT '市场id',
  `history_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '产生时间',
  `checkin_status` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=393 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `third_hangguo_trade_data`
--

DROP TABLE IF EXISTS `third_hangguo_trade_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `third_hangguo_trade_data` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '交易日期',
  `supplier_no` varchar(50) DEFAULT NULL COMMENT '供应商码',
  `supplier_name` varchar(50) DEFAULT NULL COMMENT '供应商姓名',
  `batch_no` varchar(30) DEFAULT NULL COMMENT '批号',
  `item_number` varchar(20) DEFAULT NULL COMMENT '商品码',
  `item_name` varchar(30) DEFAULT NULL COMMENT '商品名',
  `unit` varchar(50) DEFAULT NULL COMMENT '规格',
  `origin_no` varchar(10) DEFAULT NULL COMMENT '产地编码',
  `origin_name` varchar(50) DEFAULT NULL COMMENT '产地名称',
  `position_no` varchar(10) DEFAULT NULL COMMENT '仓位码',
  `position_name` varchar(50) DEFAULT NULL COMMENT '仓位名称',
  `price` decimal(18,2) DEFAULT NULL COMMENT '成交价格',
  `package_number` int DEFAULT '0' COMMENT '件数',
  `number` int DEFAULT '0' COMMENT '成交数量',
  `amount` decimal(18,2) DEFAULT NULL COMMENT '成交金额',
  `weight` decimal(10,3) DEFAULT NULL COMMENT '箱重',
  `trade_no` varchar(50) DEFAULT NULL COMMENT '流水号',
  `pos_no` varchar(50) DEFAULT NULL COMMENT 'POS 机号',
  `pay_way` varchar(10) DEFAULT NULL COMMENT '支付方式',
  `member_no` varchar(50) DEFAULT NULL COMMENT '会员卡号',
  `member_name` varchar(50) DEFAULT NULL COMMENT '会员姓名',
  `total_amount` decimal(18,2) DEFAULT NULL COMMENT '总金额',
  `operator` varchar(50) DEFAULT NULL COMMENT '营业员',
  `payer` varchar(50) DEFAULT NULL COMMENT '收款员',
  `pay_no` varchar(50) DEFAULT NULL COMMENT '支付流水号',
  `register_no` varchar(20) DEFAULT NULL COMMENT '报备单号',
  `handle_flag` tinyint DEFAULT '1' COMMENT '处理标志位1未处理/2已处理/3无需处理',
  `report_flag` tinyint DEFAULT '1' COMMENT '上报标志位1未上报/2已上报/3无需上报',
  `handle_remark` varchar(200) DEFAULT NULL COMMENT '处理备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `applets_config`
--

DROP TABLE IF EXISTS `applets_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `applets_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `app_id` varchar(50) CHARACTER SET latin1 DEFAULT NULL COMMENT '小程序id',
  `app_secret` varchar(100) CHARACTER SET latin1 DEFAULT NULL COMMENT '小程序Secret',
  `access_token` varchar(1000) CHARACTER SET latin1 DEFAULT NULL COMMENT '小程序accessToken',
  `access_token_expires_in` int DEFAULT NULL COMMENT '小程序accessToken有效时间',
  `access_token_update_time` datetime DEFAULT NULL COMMENT '小程序accessToken更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `trade_request`
--

DROP TABLE IF EXISTS `trade_request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trade_request` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `buyer_id` bigint DEFAULT NULL COMMENT '买家ID',
  `buyer_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `seller_id` bigint DEFAULT NULL COMMENT '卖家ID',
  `seller_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `trade_weight` decimal(10,3) NOT NULL COMMENT '交易重量',
  `product_stock_id` bigint DEFAULT NULL COMMENT '商品库存ID',
  `trade_order_Id` bigint NOT NULL COMMENT '订单ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `return_status` int DEFAULT NULL COMMENT '退货状态',
  `reason` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '原因',
  `code` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '编号',
  `weight_unit` int NOT NULL DEFAULT '10' COMMENT '重量单位',
  `spec_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '规格',
  `product_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '商品名称',
  `handle_time` timestamp NULL DEFAULT NULL,
  `buyer_market_id` bigint DEFAULT '1' COMMENT '买家市场ID',
  `seller_market_id` bigint DEFAULT '1' COMMENT '卖家市场ID',
  `source_type` tinyint(1) DEFAULT '1' COMMENT '来源类型 1农溯安/2杭果交易数据',
  `report_flag` tinyint(1) DEFAULT '1' COMMENT '是否上报 1上报/2不上报',
  `third_trade_no` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '第三方交易流水号',
  `batch_no` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '批号',
  `origin_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '产地名称',
  `position_no` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '仓位码',
  `position_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '仓位名称',
  `price` decimal(10,3) DEFAULT '0.000' COMMENT '成交价格',
  `package_number` int DEFAULT '0' COMMENT '件数',
  `number` int DEFAULT '0' COMMENT '成交数量',
  `amount` decimal(10,3) DEFAULT '0.000' COMMENT '成交金额',
  `pos_no` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT 'POS机号',
  `pay_way` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '支付方式',
  `total_amount` decimal(10,3) DEFAULT '0.000' COMMENT '总金额',
  `operator` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '营业员',
  `payer` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '收款员',
  `pay_no` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '支付流水号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_qr_history`
--

DROP TABLE IF EXISTS `user_qr_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_qr_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL COMMENT '用户(商户)ID',
  `user_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `qr_status` int NOT NULL COMMENT '二维码状态',
  `content` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '二维码转换信息内容',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `bill_id` bigint DEFAULT NULL COMMENT '报备单ID',
  `verify_status` int NOT NULL DEFAULT '0' COMMENT '查验状态',
  `is_valid` int NOT NULL DEFAULT '1' COMMENT '是否有效',
  `trade_request_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `approver_info`
--

DROP TABLE IF EXISTS `approver_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `approver_info` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '审核人名字',
  `user_id` bigint NOT NULL COMMENT '审核人ID',
  `phone` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '审核人电话',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `market_id` bigint DEFAULT NULL COMMENT '市场id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `check_order`
--

DROP TABLE IF EXISTS `check_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `check_order` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `id_card` varchar(100) DEFAULT NULL COMMENT '经营户统一社会信用代码/自然人经营户个人身份证号',
  `user_id` bigint DEFAULT NULL COMMENT '检测用户id',
  `user_name` varchar(50) DEFAULT NULL COMMENT '检测用户名称',
  `tally_area_nos` varchar(200) DEFAULT NULL COMMENT '检测用户摊位号',
  `check_no` varchar(20) DEFAULT NULL COMMENT '检测批次号',
  `check_org_code` varchar(50) DEFAULT NULL COMMENT '检测机构编号',
  `check_org_name` varchar(50) DEFAULT NULL COMMENT '检测机构名称',
  `check_result` tinyint(1) DEFAULT NULL COMMENT '检测结果 0合格：1不合格',
  `check_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '检测时间',
  `check_type` tinyint(1) DEFAULT NULL COMMENT '检测类型 1.定性，2.定量',
  `checker_id` bigint DEFAULT NULL COMMENT '检测人id',
  `checker` varchar(100) DEFAULT NULL COMMENT '检测人',
  `goods_name` varchar(20) DEFAULT NULL COMMENT '本地商品名字',
  `goods_code` varchar(50) DEFAULT NULL COMMENT '本地商品编码',
  `market_id` bigint DEFAULT NULL COMMENT '市场id',
  `report_flag` tinyint(1) DEFAULT '-1' COMMENT '上报标志位-1未处理/1待上报/2已上报',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `seperate_print_report`
--

DROP TABLE IF EXISTS `seperate_print_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seperate_print_report` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '编号',
  `bill_id` bigint NOT NULL COMMENT '登记单ID',
  `approver_info_id` bigint NOT NULL COMMENT '签名人ID',
  `seperate_recocrd_id` bigint NOT NULL COMMENT 'ID',
  `product_alias_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '商品别名',
  `sales_weight` decimal(18,3) DEFAULT NULL COMMENT '商品重量',
  `sales_plate` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '车牌',
  `sales_user_name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '分销商名',
  `tally_area_no` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '理货区号',
  `valid_period` int NOT NULL DEFAULT '0' COMMENT '有效期',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `operator_id` bigint NOT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '操作人姓名',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `quality_trace_trade_bill_syncpoint`
--

DROP TABLE IF EXISTS `quality_trace_trade_bill_syncpoint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `quality_trace_trade_bill_syncpoint` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bill_id` bigint DEFAULT NULL COMMENT '流水号',
  `order_id` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '订单号',
  `order_version` int NOT NULL DEFAULT '2' COMMENT '订单版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=322227 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_message`
--

DROP TABLE IF EXISTS `event_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_message` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(20) DEFAULT NULL,
  `overview` varchar(50) DEFAULT NULL,
  `content` varchar(150) DEFAULT NULL,
  `source_business_id` bigint DEFAULT NULL COMMENT '关联业务id',
  `source_business_type` tinyint NOT NULL COMMENT '关联业务类型 用户 10 报备 20 交易 30 ;',
  `creator_id` bigint DEFAULT NULL,
  `creator` varchar(50) DEFAULT NULL,
  `receiver_id` bigint DEFAULT NULL COMMENT '消息接收者id',
  `receiver` varchar(50) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `read_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '已读标志 未读0 已读 1',
  `receiver_type` tinyint NOT NULL COMMENT '接收者角色 普通用户10 管理员20',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_stock`
--

DROP TABLE IF EXISTS `product_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_stock` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `preserve_type` int NOT NULL DEFAULT '10' COMMENT '保存类型',
  `stock_weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '库存重量',
  `total_weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '总重量',
  `weight_unit` int NOT NULL DEFAULT '10' COMMENT '重量单位',
  `spec_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '规格',
  `product_name` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '商品名称',
  `product_id` bigint NOT NULL,
  `user_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '业户姓名',
  `user_id` bigint DEFAULT NULL COMMENT '理货区用户ID',
  `brand_id` bigint DEFAULT NULL COMMENT '品牌ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `trade_detail_num` int NOT NULL DEFAULT '0' COMMENT '可用批次数量',
  `brand_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '品牌名称',
  `market_id` bigint DEFAULT '1' COMMENT '市场ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `r_user_category`
--

DROP TABLE IF EXISTS `r_user_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `r_user_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `category_name` varchar(100) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `category_type` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=139 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 NOT NULL,
  `phone` varchar(15) CHARACTER SET utf8 NOT NULL,
  `card_no` varchar(20) CHARACTER SET utf8 NOT NULL,
  `addr` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `card_no_front_url` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '身份证正面',
  `card_no_back_url` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '身份证反面',
  `tally_area_nos` varchar(200) CHARACTER SET utf8 DEFAULT NULL,
  `business_license_url` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '营业执照',
  `sales_city_id` bigint DEFAULT NULL,
  `sales_city_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `state` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:启用 0：禁用',
  `password` varchar(50) CHARACTER SET utf8 NOT NULL,
  `yn` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1:正常 -1：删除',
  `version` tinyint NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_delete` bigint DEFAULT '0' COMMENT '是否删除',
  `qr_status` int DEFAULT '0' COMMENT '二维码状态(默认红色)',
  `user_type` tinyint DEFAULT NULL COMMENT '用户类型 个人 10 企业 20',
  `market_id` bigint DEFAULT '1' COMMENT '市场ID',
  `license` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '统一信用代码',
  `legal_person` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '法人姓名',
  `manufacturing_license_url` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '生产许可证',
  `operation_license_url` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '经营许可证',
  `vocation_type` tinyint DEFAULT NULL COMMENT '批发 10 农贸 20 团体 30 个人40 餐饮50 配送商 60',
  `validate_state` tinyint NOT NULL DEFAULT '10' COMMENT '未实名10 待审核 20  审核未通过 30 审核通过 40',
  `market_name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `business_category_ids` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '经营品类',
  `business_categories` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '经营品类',
  `source` tinyint DEFAULT NULL COMMENT '来源 下游企业：10，注册：20',
  `pre_qr_status` int DEFAULT NULL COMMENT '前一次二维码值',
  `is_active` tinyint(1) DEFAULT '1' COMMENT '-1去活跃/1活跃',
  `third_party_code` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '第三方编码',
  `open_id` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '微信openId',
  `confirm_date` datetime DEFAULT NULL COMMENT '提示微信绑定，确认不再弹出日期',
  `is_push` tinyint(1) DEFAULT '-1' COMMENT '上报标志位，1待上报，-1无需上报',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_unique_phone` (`market_id`,`phone`,`is_delete`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=274 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='来源 下游企业：10，注册：20\r\nsource';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_driver_ref`
--

DROP TABLE IF EXISTS `user_driver_ref`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_driver_ref` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `driver_id` bigint DEFAULT NULL,
  `driver_name` varchar(36) DEFAULT NULL,
  `seller_id` bigint DEFAULT NULL,
  `seller_name` varchar(36) DEFAULT NULL,
  `share_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `tally_area_nos` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `separate_sales_record`
--

DROP TABLE IF EXISTS `separate_sales_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `separate_sales_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sales_city_id` bigint NOT NULL COMMENT '分销城市',
  `sales_city_name` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '分销城市',
  `sales_user_id` bigint DEFAULT NULL,
  `sales_user_name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '分销人',
  `sales_plate` varchar(15) CHARACTER SET utf8 DEFAULT NULL COMMENT '分销车牌号',
  `sales_weight` decimal(10,3) NOT NULL COMMENT '分销重量KG',
  `register_bill_code` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '被分销的登记单编码',
  `trade_no` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `tally_area_no` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '理货区号',
  PRIMARY KEY (`id`),
  KEY `REGISTER_BILL_CODE_INDEX` (`register_bill_code`) USING BTREE,
  KEY `TRADE_NO_INDEX` (`trade_no`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `biz_number`
--

DROP TABLE IF EXISTS `biz_number`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `biz_number` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '业务类型',
  `value` bigint DEFAULT NULL COMMENT '编号值',
  `memo` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `version` bigint DEFAULT NULL COMMENT '版本号',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='业务号\r\n记录所有业务的编号\r\n如：\r\n回访编号:HF201712080001';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `brand`
--

DROP TABLE IF EXISTS `brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `brand` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `brand_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '品牌名称',
  `user_id` bigint DEFAULT NULL COMMENT '用户ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `market_id` bigint DEFAULT '1' COMMENT '市场ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1170 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `detect_record`
--

DROP TABLE IF EXISTS `detect_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `detect_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `detect_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '检测时间',
  `detect_operator` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '检测人',
  `detect_type` tinyint(1) NOT NULL COMMENT '1.第一次送检 2：复检',
  `detect_state` tinyint(1) NOT NULL COMMENT '1.合格 2.不合格',
  `pd_result` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '检测项的结果',
  `register_bill_code` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '登记单编号',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `detect_batch_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '检测批号',
  `normal_result` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '标准值',
  `detect_company` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '检测机构',
  `detect_request_id` bigint DEFAULT NULL COMMENT '检测请求ID',
  PRIMARY KEY (`id`),
  KEY `REGISTER_BILL_CODE_INDEX` (`register_bill_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `instructions` varchar(100) DEFAULT NULL,
  `opt_type` varchar(100) DEFAULT NULL,
  `opt_category` varchar(100) DEFAULT NULL,
  `opt_value` varchar(100) DEFAULT NULL,
  `market_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `checkinout_record`
--

DROP TABLE IF EXISTS `checkinout_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `checkinout_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `status` int NOT NULL COMMENT '进门状态',
  `inout` int NOT NULL COMMENT '进出门',
  `remark` varchar(500) CHARACTER SET utf8 DEFAULT NULL COMMENT '备注',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作人ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '业户名称',
  `product_name` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '商品名称',
  `inout_weight` decimal(10,3) NOT NULL DEFAULT '0.000' COMMENT '进出门重量',
  `trade_detail_id` bigint NOT NULL DEFAULT '0' COMMENT '分销ID',
  `verify_status` int NOT NULL DEFAULT '0' COMMENT '查验状态',
  `bill_type` int NOT NULL DEFAULT '10' COMMENT '报备类型',
  `bill_id` bigint DEFAULT NULL COMMENT '报备单ID',
  `weight_unit` int NOT NULL DEFAULT '1' COMMENT '重量单位',
  `user_id` bigint DEFAULT NULL COMMENT '业户ID',
  `market_id` bigint DEFAULT '1' COMMENT '市场ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=158 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `countries`
--

DROP TABLE IF EXISTS `countries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `countries` (
  `id` smallint unsigned NOT NULL AUTO_INCREMENT,
  `code` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '地区代码',
  `cname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `countries_code_index` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `upstream`
--

DROP TABLE IF EXISTS `upstream`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upstream` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `upstream_type` int NOT NULL COMMENT '10个人 20.企业',
  `id_card` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '身份证号',
  `telphone` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '联系方式',
  `name` varchar(50) CHARACTER SET utf8 NOT NULL COMMENT '企业(个人)名称',
  `legal_person` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '法人姓名',
  `license` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '统一信用代码',
  `business_license_url` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '企业营业执照',
  `manufacturing_license_url` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '生产许可证',
  `operation_license_url` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '经营许可证',
  `card_no_front_url` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '身份证照正面',
  `card_no_back_url` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '身份证照反面URL',
  `source_user_id` bigint DEFAULT NULL COMMENT '复制来源userid',
  `operator_id` bigint DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作人姓名',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `upORdown` tinyint DEFAULT '10' COMMENT '上游企业10 下游企业20',
  `market_id` bigint DEFAULT '1' COMMENT '市场ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=207 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `register_bill`
--

DROP TABLE IF EXISTS `register_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `register_bill` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `code` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '编号',
  `sample_code` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '采样编号',
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '业户姓名',
  `id_card_no` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '身份证号',
  `addr` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '地址',
  `phone` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `user_id` bigint DEFAULT NULL COMMENT '理货区用户ID',
  `plate` varchar(15) CHARACTER SET utf8 DEFAULT NULL COMMENT '车牌',
  `state` tinyint(1) DEFAULT NULL COMMENT '1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中，8.审核未通过',
  `sales_type` tinyint(1) DEFAULT NULL COMMENT '1.分销 2.全销',
  `product_name` varchar(20) CHARACTER SET utf8 NOT NULL COMMENT '商品名称',
  `product_id` bigint NOT NULL,
  `origin_id` bigint DEFAULT NULL COMMENT '产地ID',
  `origin_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '产地',
  `weight` decimal(15,3) NOT NULL DEFAULT '0.000' COMMENT '总重量',
  `operator_id` bigint DEFAULT NULL,
  `operator_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '操作人ID',
  `sample_source` tinyint(1) DEFAULT NULL COMMENT '1:采样检测 2:主动送检',
  `detect_state` tinyint(1) DEFAULT NULL COMMENT '默认null,1.合格 2.不合格 3.复检合格 4.复检不合格',
  `latest_detect_record_id` bigint DEFAULT NULL COMMENT '检测记录ID',
  `latest_detect_operator` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '检测人员',
  `latest_detect_time` timestamp NULL DEFAULT NULL COMMENT '检测时间',
  `latest_pd_result` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最新一次检测值',
  `version` tinyint NOT NULL DEFAULT '0' COMMENT '版本号',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `upstream_id` bigint DEFAULT NULL COMMENT '上游信息ID',
  `complete` int DEFAULT NULL COMMENT '信息是否完整',
  `verify_status` int NOT NULL DEFAULT '0' COMMENT '查验状态, "待审核"0, "已退回10, "已通过20, "不通过30',
  `weight_unit` int NOT NULL DEFAULT '1' COMMENT '重量单位',
  `preserve_type` int NOT NULL DEFAULT '10' COMMENT '保存类型',
  `verify_type` int NOT NULL DEFAULT '0' COMMENT '查验类型',
  `spec_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '规格',
  `bill_type` int NOT NULL DEFAULT '10' COMMENT '报备类型',
  `brand_id` bigint DEFAULT NULL COMMENT '品牌ID',
  `brand_name` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '品牌名称',
  `truck_type` int NOT NULL DEFAULT '10' COMMENT '车类型',
  `tally_area_no` varchar(60) CHARACTER SET utf8 DEFAULT NULL COMMENT '摊位号',
  `reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '原因',
  `is_checkin` int NOT NULL DEFAULT '-1' COMMENT '是否进门',
  `checkin_status` int NOT NULL DEFAULT '0' COMMENT '进门状态',
  `checkout_status` int NOT NULL DEFAULT '0' COMMENT '出门状态',
  `is_deleted` int NOT NULL DEFAULT '0' COMMENT '是否被删除',
  `operation_time` datetime DEFAULT NULL COMMENT '操作时间',
  `order_type` int DEFAULT NULL,
  `market_id` bigint DEFAULT '1' COMMENT '市场ID',
  `register_head_code` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '主台账编号',
  `third_party_code` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '经营户卡号',
  `area` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '区号',
  `measure_type` tinyint DEFAULT '20' COMMENT '计量类型。10-计件 20-计重。默认计件。',
  `piece_num` decimal(10,3) DEFAULT NULL COMMENT '件数',
  `piece_weight` decimal(11,3) DEFAULT NULL COMMENT '件重',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注',
  `create_user` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
  `delete_user` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '作废人',
  `delete_time` datetime DEFAULT NULL COMMENT '作废时间',
  `packaging` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '包装',
  `register_source` tinyint NOT NULL COMMENT '1.理货区 2.交易区',
  `trade_account` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '交易区账号',
  `trade_printing_card` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `trade_type_id` varchar(10) CHARACTER SET utf8 DEFAULT NULL,
  `trade_type_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `detect_report_url` varchar(1000) CHARACTER SET utf8 DEFAULT NULL,
  `origin_certifiy_url` varchar(1000) CHARACTER SET utf8 DEFAULT NULL,
  `exe_machine_no` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '检测仪器码',
  `handle_result_url` varchar(1000) CHARACTER SET utf8 DEFAULT NULL,
  `handle_result` varchar(4000) CHARACTER SET utf8 DEFAULT NULL,
  `creation_source` int DEFAULT '10' COMMENT '数据创建来源',
  `check_sheet_id` bigint DEFAULT NULL COMMENT '检测报告单ID',
  `corporate_name` varchar(100) CHARACTER SET utf8 DEFAULT NULL COMMENT '企业名称',
  `product_alias_name` varchar(20) CHARACTER SET utf8 DEFAULT NULL COMMENT '商品别名',
  `has_detect_report` int NOT NULL DEFAULT '0',
  `has_origin_certifiy` int NOT NULL DEFAULT '0',
  `has_handle_result` int NOT NULL DEFAULT '0',
  `detect_request_id` bigint DEFAULT NULL COMMENT '检测请求ID',
  `detect_status` int DEFAULT NULL,
  `source_id` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `source_name` varchar(100) CHARACTER SET utf8 DEFAULT NULL,
  `creator_role` tinyint DEFAULT '0' COMMENT '创建人角色。0-经营户 1-管理员',
  `regist_type` int NOT NULL DEFAULT '10' COMMENT '报备方式',
  `truck_tare_weight` decimal(11,3) DEFAULT NULL,
  `unit_price` decimal(10,3) DEFAULT NULL COMMENT '单价',
  `is_print_checksheet` int DEFAULT '0' COMMENT '是否打印',
  `tare_weight` decimal(11,3) DEFAULT NULL COMMENT '皮重',
  `return_reason` varchar(100) DEFAULT NULL COMMENT '检测退回原因',
  PRIMARY KEY (`id`),
  UNIQUE KEY `register_bill_unique_code` (`market_id`,`code`) USING BTREE,
  UNIQUE KEY `register_bill_unique_samplecode` (`sample_code`),
  KEY `ix_created` (`created`,`product_name`)
) ENGINE=InnoDB AUTO_INCREMENT=260 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-01-27 15:12:44
