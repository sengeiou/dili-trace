
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for applets_config
-- ----------------------------
DROP TABLE IF EXISTS `applets_config`;
CREATE TABLE `applets_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `app_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''小程序id'',
  `app_secret` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''小程序Secret'',
  `access_token` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''小程序accessToken'',
  `access_token_expires_in` int(10) NULL DEFAULT NULL COMMENT ''小程序accessToken有效时间'',
  `access_token_update_time` datetime(0) NULL DEFAULT NULL COMMENT ''小程序accessToken更新时间'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for approver_info
-- ----------------------------
DROP TABLE IF EXISTS `approver_info`;
CREATE TABLE `approver_info`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''审核人名字'',
  `user_id` bigint(20) NOT NULL COMMENT ''审核人ID'',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''审核人电话'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for bak_third_party_push_data_0908
-- ----------------------------
DROP TABLE IF EXISTS `bak_third_party_push_data_0908`;
CREATE TABLE `bak_third_party_push_data_0908`  (
  `id` bigint(20) NOT NULL DEFAULT 0,
  `interface_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `table_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `push_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0)
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for base64_signature
-- ----------------------------
DROP TABLE IF EXISTS `base64_signature`;
CREATE TABLE `base64_signature`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `approver_info_id` bigint(20) NOT NULL COMMENT ''审核人ID'',
  `base64` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''审核人签名Base64图片'',
  `order_num` int(11) NOT NULL COMMENT ''顺序'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for biz_number
-- ----------------------------
DROP TABLE IF EXISTS `biz_number`;
CREATE TABLE `biz_number`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''业务类型'',
  `value` bigint(20) NULL DEFAULT NULL COMMENT ''编号值'',
  `memo` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''备注'',
  `version` bigint(20) NULL DEFAULT NULL COMMENT ''版本号'',
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT ''修改时间'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = ''业务号\r\n记录所有业务的编号\r\n如：\r\n回访编号:HF201712080001'' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for brand
-- ----------------------------
DROP TABLE IF EXISTS `brand`;
CREATE TABLE `brand`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `brand_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''品牌名称'',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT ''用户ID'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `market_id` bigint(20) NULL DEFAULT 1 COMMENT ''市场ID'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT ''上一级ID'',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''名称'',
  `full_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''全名'',
  `level` int(11) NOT NULL DEFAULT 1 COMMENT ''层级'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `market_id` bigint(20) NULL DEFAULT 1 COMMENT ''市场ID'',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''第三方编码'',
  `is_show` tinyint(1) NULL DEFAULT 1 COMMENT ''登记显示,1显示/2不显示'',
  `type` tinyint(1) NULL DEFAULT 1 COMMENT ''商品类型;1杭水/2检测商品'',
  `specification` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''商品规格名'',
  `parent_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''父级第三方编码'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15648 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for check_data
-- ----------------------------
DROP TABLE IF EXISTS `check_data`;
CREATE TABLE `check_data`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `check_id` bigint(20) NOT NULL,
  `project` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''检测项名称'',
  `normal_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''检测标准值'',
  `result` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''检测结果'',
  `value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''检测数据值'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for check_order
-- ----------------------------
DROP TABLE IF EXISTS `check_order`;
CREATE TABLE `check_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `id_card` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''经营户统一社会信用代码/自然人经营户个人身份证号'',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT ''检测用户id'',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''检测用户名称'',
  `tally_area_nos` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''检测用户摊位号'',
  `check_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''检测批次号'',
  `check_org_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''检测机构编号'',
  `check_org_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''检测机构名称'',
  `check_result` tinyint(1) NULL DEFAULT NULL COMMENT ''检测结果 0合格：1不合格'',
  `check_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT ''检测时间'',
  `check_type` tinyint(1) NULL DEFAULT NULL COMMENT ''检测类型 1.定性，2.定量'',
  `checker_id` bigint(20) NULL DEFAULT NULL COMMENT ''检测人id'',
  `checker` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''检测人'',
  `goods_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''本地商品名字'',
  `goods_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''本地商品编码'',
  `market_id` bigint(20) NULL DEFAULT NULL COMMENT ''市场id'',
  `report_flag` tinyint(1) NULL DEFAULT -1 COMMENT ''上报标志位-1未处理/1待上报/2已上报'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for check_order_dispose
-- ----------------------------
DROP TABLE IF EXISTS `check_order_dispose`;
CREATE TABLE `check_order_dispose`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `check_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''检测批次号'',
  `market_id` bigint(20) NULL DEFAULT NULL COMMENT ''市场id'',
  `dispose_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT ''处置日期'',
  `dispose_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''处置备注'',
  `dispose_num` int(11) NULL DEFAULT 0 COMMENT ''处置数量'',
  `dispose_result` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''处置结果'',
  `dispose_type` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''处置类型'',
  `disposer_id` bigint(20) NULL DEFAULT NULL COMMENT ''处置人id'',
  `disposer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''处置人'',
  `report_flag` tinyint(1) NULL DEFAULT -1 COMMENT ''上报标志位-1未处理/1待上报/2已上报'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for check_sheet
-- ----------------------------
DROP TABLE IF EXISTS `check_sheet`;
CREATE TABLE `check_sheet`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''编号'',
  `id_card_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''提交人身份证号'',
  `user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''提交人姓名'',
  `valid_period` int(11) NOT NULL COMMENT ''有效天数'',
  `detect_operator_id` bigint(20) NOT NULL COMMENT ''检测人ID'',
  `detect_operator_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''检测人姓名'',
  `qrcode_url` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''二维码url'',
  `approver_info_id` bigint(20) NOT NULL COMMENT ''审核人ID'',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''备注'',
  `operator_id` bigint(20) NOT NULL COMMENT ''操作人ID'',
  `operator_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''操作人姓名'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for check_sheet_detail
-- ----------------------------
DROP TABLE IF EXISTS `check_sheet_detail`;
CREATE TABLE `check_sheet_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `check_sheet_id` bigint(20) NOT NULL COMMENT ''检测报告单ID'',
  `register_bill_id` bigint(20) NOT NULL COMMENT ''登记单ID'',
  `product_id` bigint(20) NOT NULL COMMENT ''商品ID'',
  `product_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''商品名称'',
  `product_alias_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''商品别名'',
  `origin_id` bigint(20) NOT NULL COMMENT ''产地ID'',
  `origin_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''产地'',
  `order_number` int(11) NOT NULL COMMENT ''序号'',
  `detect_state` int(11) NOT NULL COMMENT ''检测状态'',
  `latest_pd_result` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''检测结果'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for checkinout_record
-- ----------------------------
DROP TABLE IF EXISTS `checkinout_record`;
CREATE TABLE `checkinout_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `status` int(11) NOT NULL COMMENT ''进门状态'',
  `inout` int(11) NOT NULL COMMENT ''进出门'',
  `remark` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''备注'',
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT ''操作人'',
  `operator_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''操作人ID'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `user_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''业户名称'',
  `product_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''商品名称'',
  `inout_weight` decimal(10, 3) NOT NULL DEFAULT 0.000 COMMENT ''进出门重量'',
  `trade_detail_id` bigint(20) NOT NULL DEFAULT 0 COMMENT ''分销ID'',
  `bill_id` bigint(20) NULL DEFAULT NULL COMMENT ''报备单ID'',
  `verify_status` int(11) NOT NULL DEFAULT 0 COMMENT ''查验状态'',
  `bill_type` int(11) NOT NULL DEFAULT 10 COMMENT ''报备类型'',
  `weight_unit` int(11) NOT NULL DEFAULT 1 COMMENT ''重量单位'',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT ''业户ID'',
  `market_id` bigint(20) NULL DEFAULT 1 COMMENT ''市场ID'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17324 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for code_generate
-- ----------------------------
DROP TABLE IF EXISTS `code_generate`;
CREATE TABLE `code_generate`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''编号类型'',
  `segment` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''当前编号段'',
  `seq` bigint(20) NULL DEFAULT NULL COMMENT ''当前编号'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `pattern` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''模式'',
  `prefix` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''前缀'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for countries
-- ----------------------------
DROP TABLE IF EXISTS `countries`;
CREATE TABLE `countries`  (
  `id` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT,
  `code` char(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT ''地区代码'',
  `cname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `countries_code_index`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for detect_record
-- ----------------------------
DROP TABLE IF EXISTS `detect_record`;
CREATE TABLE `detect_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `detect_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''检测时间'',
  `detect_operator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''检测人'',
  `detect_type` tinyint(1) NOT NULL COMMENT ''1.第一次送检 2：复检'',
  `detect_state` tinyint(1) NOT NULL COMMENT ''1.合格 2.不合格'',
  `pd_result` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''检测项的结果'',
  `register_bill_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''登记单编号'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `REGISTER_BILL_CODE_INDEX`(`register_bill_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for event_message
-- ----------------------------
DROP TABLE IF EXISTS `event_message`;
CREATE TABLE `event_message`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `overview` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `content` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `source_business_id` bigint(20) NULL DEFAULT NULL COMMENT ''关联业务id'',
  `source_business_type` tinyint(2) NOT NULL COMMENT ''关联业务类型 用户 10 报备 20 交易 30 ;'',
  `creator_id` bigint(20) NULL DEFAULT NULL,
  `creator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `receiver_id` bigint(20) NULL DEFAULT NULL COMMENT ''消息接收者id'',
  `receiver` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `read_flag` tinyint(1) NOT NULL DEFAULT 0 COMMENT ''已读标志 未读0 已读 1'',
  `receiver_type` tinyint(2) NOT NULL COMMENT ''接收者角色 普通用户10 管理员20'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 876009 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for image_cert
-- ----------------------------
DROP TABLE IF EXISTS `image_cert`;
CREATE TABLE `image_cert`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NULL DEFAULT NULL COMMENT ''所属数据ID'',
  `url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''图片URL'',
  `cert_type` int(11) NOT NULL COMMENT ''图片类型'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `bill_type` tinyint(2) NULL DEFAULT 1 COMMENT ''单据类型。1-报备单 2-检测单 3-检测不合格处置单 4-进门主台账单。默认为1'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21938 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for market
-- ----------------------------
DROP TABLE IF EXISTS `market`;
CREATE TABLE `market`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''市场名称'',
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT ''操作人'',
  `operator_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''操作人ID'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `app_id` bigint(20) NULL DEFAULT NULL COMMENT ''第三方对接平台应用ID'',
  `app_secret` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''第三方对接平台应用秘钥'',
  `context_url` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''第三方对接平台url地址'',
  `platform_market_id` bigint(20) NULL DEFAULT NULL COMMENT ''第三方对接平台市场ID'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for message_config
-- ----------------------------
DROP TABLE IF EXISTS `message_config`;
CREATE TABLE `message_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `operation` tinyint(4) NULL DEFAULT NULL COMMENT ''10:商户注册提交;11:商户注册审核通过;12:商户注册审核未通过;20:提交报备;21:报备审核通过;22:报备审核未通过;23:报备审核退回;30:进门审核;40:卖家下单;50:买家下单;51:卖家确认订单'',
  `message_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''是否发站内信'',
  `sms_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''是否发短信'',
  `wechat_flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''是否发微信'',
  `sms_scene_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''短信中心sceneCode'',
  `wechat_template_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''微信模板id'',
  `event_message_title` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''站内消息标题'',
  `event_message_content` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''站内消息内容'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for product_stock
-- ----------------------------
DROP TABLE IF EXISTS `product_stock`;
CREATE TABLE `product_stock`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `preserve_type` int(11) NOT NULL DEFAULT 10 COMMENT ''保存类型'',
  `stock_weight` decimal(10, 3) NOT NULL DEFAULT 0.000 COMMENT ''库存重量'',
  `total_weight` decimal(10, 3) NOT NULL DEFAULT 0.000 COMMENT ''总重量'',
  `weight_unit` int(11) NOT NULL DEFAULT 10 COMMENT ''重量单位'',
  `spec_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''规格'',
  `product_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''商品名称'',
  `product_id` bigint(20) NOT NULL,
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''业户姓名'',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT ''理货区用户ID'',
  `brand_id` bigint(20) NULL DEFAULT NULL COMMENT ''品牌ID'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `trade_detail_num` int(11) NOT NULL DEFAULT 0 COMMENT ''可用批次数量'',
  `brand_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''品牌名称'',
  `market_id` bigint(20) NULL DEFAULT 1 COMMENT ''市场ID'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2081 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for quality_trace_trade_bill
-- ----------------------------
DROP TABLE IF EXISTS `quality_trace_trade_bill`;
CREATE TABLE `quality_trace_trade_bill`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NOT NULL COMMENT ''流水号'',
  `register_bill_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sales_type` tinyint(1) NULL DEFAULT NULL COMMENT ''1.分销 2.全销'',
  `order_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''订单号'',
  `order_status` tinyint(1) NULL DEFAULT NULL,
  `seller_account` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''卖家账号'',
  `seller_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''卖家名称'',
  `sellerIDNo` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `buyer_account` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''买家账号'',
  `buyer_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''买家名称'',
  `buyerIDNo` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `order_create_date` datetime(0) NULL DEFAULT NULL COMMENT ''订单创建时间'',
  `order_pay_date` datetime(0) NULL DEFAULT NULL COMMENT ''订单支付时间'',
  `pdResult` decimal(18, 2) NULL DEFAULT NULL COMMENT ''残留值'',
  `conclusion` tinyint(1) NULL DEFAULT NULL COMMENT ''合格值  0-未知 1合格  2不合格 3作废'',
  `check_result_EID` bigint(20) NULL DEFAULT NULL COMMENT ''检测结算单唯一标志,NULL表示无检测信息'',
  `trade_flow_id` bigint(20) NULL DEFAULT NULL COMMENT ''交易号'',
  `total_money` bigint(30) NULL DEFAULT NULL COMMENT ''总金额'',
  `order_item_id` bigint(20) NULL DEFAULT NULL COMMENT ''订单项号'',
  `product_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''商品名称'',
  `cate_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''品类名称'',
  `price` bigint(30) NULL DEFAULT NULL COMMENT ''单价(分)'',
  `piece_quantity` bigint(30) NULL DEFAULT NULL COMMENT ''件数'',
  `piece_weight` bigint(20) NULL DEFAULT NULL COMMENT ''件重(公斤)'',
  `net_weight` bigint(20) NULL DEFAULT NULL COMMENT ''总净重(公斤)'',
  `tradetype_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''交易类型编码'',
  `tradetype_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''交易类型名称'',
  `bill_active` int(255) NULL DEFAULT NULL COMMENT ''状态'',
  `sale_unit` int(1) NULL DEFAULT NULL COMMENT ''销售单位 1:斤 2：件'',
  `match_status` int(11) NULL DEFAULT NULL COMMENT ''匹配状态'',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `BILL_ID_UNIQUE_INDEX`(`bill_id`) USING BTREE,
  INDEX `ORDER_ID_INDEX`(`order_id`) USING BTREE,
  INDEX `REGISTER_BILL_CODE_INDEX`(`register_bill_code`) USING BTREE,
  INDEX `ix_match_st`(`match_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for quality_trace_trade_bill_syncpoint
-- ----------------------------
DROP TABLE IF EXISTS `quality_trace_trade_bill_syncpoint`;
CREATE TABLE `quality_trace_trade_bill_syncpoint`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NULL DEFAULT NULL COMMENT ''流水号'',
  `order_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''订单号'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_user_category
-- ----------------------------
DROP TABLE IF EXISTS `r_user_category`;
CREATE TABLE `r_user_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `category_id` bigint(20) NULL DEFAULT NULL,
  `category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `category_type` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 860 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_user_tally_area
-- ----------------------------
DROP TABLE IF EXISTS `r_user_tally_area`;
CREATE TABLE `r_user_tally_area`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tally_area_no_id` bigint(20) NULL DEFAULT NULL COMMENT ''摊位号ID'',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT ''用户ID'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 833 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_user_upstream
-- ----------------------------
DROP TABLE IF EXISTS `r_user_upstream`;
CREATE TABLE `r_user_upstream`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT ''用户(商户)ID'',
  `upstream_id` bigint(20) NOT NULL COMMENT ''上游信息ID'',
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT ''操作人'',
  `operator_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''操作人ID'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 979 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for register_bill
-- ----------------------------
DROP TABLE IF EXISTS `register_bill`;
CREATE TABLE `register_bill`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''编号'',
  `sample_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''采样编号'',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''业户姓名'',
  `id_card_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''身份证号'',
  `addr` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''地址'',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT ''理货区用户ID'',
  `plate` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''车牌'',
  `state` tinyint(1) NOT NULL COMMENT ''1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中，8.审核未通过'',
  `sales_type` tinyint(1) NULL DEFAULT NULL COMMENT ''1.分销 2.全销'',
  `product_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''商品名称'',
  `product_id` bigint(20) NOT NULL,
  `origin_id` bigint(20) NULL DEFAULT NULL COMMENT ''产地ID'',
  `origin_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''产地'',
  `weight` decimal(10, 3) NOT NULL DEFAULT 0.000 COMMENT ''重量'',
  `operator_id` bigint(20) NULL DEFAULT NULL,
  `operator_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''操作人ID'',
  `sample_source` tinyint(1) NULL DEFAULT NULL COMMENT ''1:采样检测 2:主动送检'',
  `detect_state` tinyint(1) NULL DEFAULT NULL COMMENT ''默认null,1.合格 2.不合格 3.复检合格 4.复检不合格'',
  `latest_detect_record_id` bigint(20) NULL DEFAULT NULL COMMENT ''检测记录ID'',
  `latest_detect_operator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''检测人员'',
  `latest_detect_time` timestamp(0) NULL DEFAULT NULL COMMENT ''检测时间'',
  `latest_pd_result` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''最新一次检测值'',
  `version` tinyint(4) NOT NULL DEFAULT 0 COMMENT ''版本号'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `upstream_id` bigint(20) NULL DEFAULT NULL COMMENT ''上游信息ID'',
  `complete` int(11) NULL DEFAULT NULL COMMENT ''信息是否完整'',
  `verify_status` int(11) NOT NULL DEFAULT 0 COMMENT ''查验状态, \"待审核\"0, \"已退回10, \"已通过20, \"不通过30'',
  `weight_unit` int(11) NOT NULL DEFAULT 1 COMMENT ''重量单位'',
  `preserve_type` int(11) NOT NULL DEFAULT 10 COMMENT ''保存类型'',
  `verify_type` int(11) NOT NULL DEFAULT 0 COMMENT ''查验类型'',
  `spec_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''规格'',
  `bill_type` int(11) NOT NULL DEFAULT 10 COMMENT ''报备类型'',
  `brand_id` bigint(20) NULL DEFAULT NULL COMMENT ''品牌ID'',
  `brand_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''品牌名称'',
  `truck_type` int(11) NOT NULL DEFAULT 10 COMMENT ''车类型'',
  `tally_area_no` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''摊位号'',
  `reason` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''原因'',
  `is_checkin` int(11) NOT NULL DEFAULT -1 COMMENT ''是否进门'',
  `checkin_status` int(11) NOT NULL DEFAULT 0 COMMENT ''进门状态'',
  `checkout_status` int(11) NOT NULL DEFAULT 0 COMMENT ''出门状态'',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT ''是否被删除'',
  `operation_time` datetime(0) NULL DEFAULT NULL COMMENT ''操作时间'',
  `market_id` bigint(20) NULL DEFAULT 1 COMMENT ''市场ID'',
  `register_head_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''主台账编号'',
  `third_party_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''经营户卡号'',
  `area` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''区号'',
  `measure_type` tinyint(2) NULL DEFAULT 20 COMMENT ''计量类型。10-计件 20-计重。默认计件。'',
  `piece_num` decimal(10, 3) NULL DEFAULT NULL COMMENT ''件数'',
  `piece_weight` decimal(10, 3) NULL DEFAULT NULL COMMENT ''件重'',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''备注'',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''创建人'',
  `delete_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''作废人'',
  `delete_time` datetime(0) NULL DEFAULT NULL COMMENT ''作废时间'',
  `packaging` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''包装'',
  `order_type` tinyint(2) NULL DEFAULT 1 COMMENT ''订单类型 1.报备单 2.进门登记单'',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `register_bill_unique_code`(`code`) USING BTREE,
  INDEX `ix_created`(`created`, `product_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20222 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for register_bill_history
-- ----------------------------
DROP TABLE IF EXISTS `register_bill_history`;
CREATE TABLE `register_bill_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_id` bigint(20) NOT NULL,
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''编号'',
  `sample_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''采样编号'',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''业户姓名'',
  `id_card_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''身份证号'',
  `addr` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''地址'',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT ''理货区用户ID'',
  `plate` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''车牌'',
  `state` tinyint(1) NOT NULL COMMENT ''1.待审核 2.待采样 3.已采样 4.待检测 5.检测中 6.已检测 7.复检中，8.审核未通过'',
  `sales_type` tinyint(1) NULL DEFAULT NULL COMMENT ''1.分销 2.全销'',
  `product_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''商品名称'',
  `product_id` bigint(20) NOT NULL,
  `origin_id` bigint(20) NULL DEFAULT NULL COMMENT ''产地ID'',
  `origin_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''产地'',
  `weight` decimal(10, 3) NOT NULL DEFAULT 0.000 COMMENT ''重量'',
  `operator_id` bigint(20) NULL DEFAULT NULL,
  `operator_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''操作人ID'',
  `sample_source` tinyint(1) NULL DEFAULT NULL COMMENT ''1:采样检测 2:主动送检'',
  `detect_state` tinyint(1) NULL DEFAULT NULL COMMENT ''默认null,1.合格 2.不合格 3.复检合格 4.复检不合格'',
  `latest_detect_record_id` bigint(20) NULL DEFAULT NULL COMMENT ''检测记录ID'',
  `latest_detect_operator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''检测人员'',
  `latest_detect_time` timestamp(0) NULL DEFAULT NULL COMMENT ''检测时间'',
  `latest_pd_result` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''最新一次检测值'',
  `version` tinyint(4) NOT NULL DEFAULT 0 COMMENT ''版本号'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `upstream_id` bigint(20) NULL DEFAULT NULL COMMENT ''上游信息ID'',
  `complete` int(11) NULL DEFAULT NULL COMMENT ''信息是否完整'',
  `verify_status` int(11) NOT NULL DEFAULT 0 COMMENT ''查验状态'',
  `weight_unit` int(11) NOT NULL DEFAULT 1 COMMENT ''重量单位'',
  `preserve_type` int(11) NOT NULL DEFAULT 10 COMMENT ''保存类型'',
  `verify_type` int(11) NOT NULL DEFAULT 0 COMMENT ''查验类型'',
  `spec_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''规格'',
  `bill_type` int(11) NOT NULL DEFAULT 10 COMMENT ''报备类型'',
  `brand_id` bigint(20) NULL DEFAULT NULL COMMENT ''品牌ID'',
  `brand_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''品牌名称'',
  `truck_type` int(11) NOT NULL DEFAULT 10 COMMENT ''车类型'',
  `tally_area_no` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''摊位号'',
  `reason` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''原因'',
  `is_checkin` int(11) NOT NULL DEFAULT -1 COMMENT ''是否进门'',
  `is_deleted` int(11) NOT NULL DEFAULT 0 COMMENT ''是否被删除'',
  `operation_time` datetime(0) NULL DEFAULT NULL COMMENT ''操作时间'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41117 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for register_head
-- ----------------------------
DROP TABLE IF EXISTS `register_head`;
CREATE TABLE `register_head`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT ''表主键'',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''主台账编号'',
  `bill_type` int(11) NOT NULL COMMENT ''单据类型。10-正常进场 20-补单 30-外冷分批进场。'',
  `user_id` bigint(20) NOT NULL COMMENT ''业户ID'',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''业户姓名'',
  `id_card_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''业户身份证号'',
  `third_party_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''经营户卡号'',
  `addr` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''业户地址'',
  `phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''业户手机'',
  `plate` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''车牌号'',
  `product_id` bigint(20) NOT NULL COMMENT ''商品id'',
  `product_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''商品名称'',
  `measure_type` tinyint(2) NOT NULL COMMENT ''计量类型。10-计件 20-计重。默认计件。'',
  `piece_num` decimal(10, 3) NULL DEFAULT NULL COMMENT ''件数'',
  `piece_weight` decimal(10, 3) NULL DEFAULT NULL COMMENT ''件重'',
  `weight` decimal(10, 3) NOT NULL COMMENT ''总重量'',
  `remain_weight` decimal(10, 3) NOT NULL COMMENT ''剩余重量'',
  `weight_unit` int(11) NOT NULL COMMENT ''重量单位。1-斤 2-公斤。默认1。'',
  `upstream_id` bigint(20) NOT NULL COMMENT ''上游id'',
  `spec_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''规格'',
  `origin_id` bigint(20) NULL DEFAULT NULL COMMENT ''产地id'',
  `origin_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''产地'',
  `brand_id` bigint(20) NULL DEFAULT NULL COMMENT ''品牌id'',
  `brand_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''品牌名称'',
  `remark` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''备注'',
  `create_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''创建人'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  `modify_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''修改人'',
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT ''修改时间'',
  `is_deleted` tinyint(2) NULL DEFAULT NULL COMMENT ''是否作废。0-否 1-是'',
  `delete_user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''作废人'',
  `delete_time` datetime(0) NULL DEFAULT NULL COMMENT ''作废时间'',
  `version` tinyint(4) NOT NULL COMMENT ''版本号'',
  `reason` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''原因'',
  `active` tinyint(2) NULL DEFAULT NULL COMMENT ''是否启用。0-否 1-是'',
  `market_id` bigint(20) NULL DEFAULT NULL COMMENT ''市场ID'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for separate_sales_record
-- ----------------------------
DROP TABLE IF EXISTS `separate_sales_record`;
CREATE TABLE `separate_sales_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sales_city_id` bigint(20) NULL DEFAULT NULL COMMENT ''分销城市'',
  `sales_city_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''分销城市'',
  `sales_user_id` bigint(20) NULL DEFAULT NULL,
  `sales_user_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''分销人'',
  `sales_plate` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''分销车牌号'',
  `sales_weight` int(11) NOT NULL COMMENT ''分销重量KG'',
  `register_bill_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''被分销的登记单编码'',
  `trade_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT ''分销自'',
  `bill_id` bigint(20) NULL DEFAULT NULL COMMENT ''最初登记单ID'',
  `store_weight` decimal(10, 3) NOT NULL DEFAULT 0.000 COMMENT ''未分销重量'',
  `sales_type` int(11) NULL DEFAULT NULL COMMENT ''分销类型'',
  `checkin_record_id` bigint(20) NULL DEFAULT NULL COMMENT ''进门ID'',
  `checkout_record_id` bigint(20) NULL DEFAULT NULL COMMENT ''出门ID'',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `REGISTER_BILL_CODE_INDEX`(`register_bill_code`) USING BTREE,
  INDEX `TRADE_NO_INDEX`(`trade_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sms_message
-- ----------------------------
DROP TABLE IF EXISTS `sms_message`;
CREATE TABLE `sms_message`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `source_business_type` tinyint(4) NULL DEFAULT NULL COMMENT ''关联业务类型 用户 10 报备 20 交易 30'',
  `source_business_id` bigint(20) NULL DEFAULT NULL COMMENT ''关联业务单据id'',
  `receive_phone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''接收短信手机号码'',
  `send_reason` tinyint(4) NULL DEFAULT NULL,
  `result_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''短信发送返回结果码'',
  `result_info` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''短信发送返回结果信息'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT ''创建时间'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `instructions` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''说明'',
  `opt_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''配置类型'',
  `opt_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''配置分类'',
  `opt_value` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''配置参数'',
  `market_id` bigint(20) NULL DEFAULT 1 COMMENT ''市场ID'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tally_area_no
-- ----------------------------
DROP TABLE IF EXISTS `tally_area_no`;
CREATE TABLE `tally_area_no`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''摊位号'',
  `street` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''街区号'',
  `area` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''区域'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `market_id` bigint(20) NULL DEFAULT 1 COMMENT ''市场ID'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 806 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for third_hangguo_trade_data
-- ----------------------------
DROP TABLE IF EXISTS `third_hangguo_trade_data`;
CREATE TABLE `third_hangguo_trade_data`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `order_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT ''交易日期'',
  `supplier_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''供应商码'',
  `supplier_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''供应商姓名'',
  `batch_no` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''批号'',
  `item_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''商品码'',
  `item_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''商品名'',
  `unit` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''规格'',
  `origin_no` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''产地编码'',
  `origin_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''产地名称'',
  `position_no` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''仓位码'',
  `position_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''仓位名称'',
  `price` decimal(18, 2) NULL DEFAULT NULL COMMENT ''成交价格'',
  `package_number` int(11) NULL DEFAULT 0 COMMENT ''件数'',
  `number` int(11) NULL DEFAULT 0 COMMENT ''成交数量'',
  `amount` decimal(18, 2) NULL DEFAULT NULL COMMENT ''成交金额'',
  `weight` decimal(10, 3) NULL DEFAULT NULL COMMENT ''箱重'',
  `trade_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''流水号'',
  `pos_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''POS 机号'',
  `pay_way` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''支付方式'',
  `member_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''会员卡号'',
  `member_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''会员姓名'',
  `total_amount` decimal(18, 2) NULL DEFAULT NULL COMMENT ''总金额'',
  `operator` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''营业员'',
  `payer` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''收款员'',
  `pay_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''支付流水号'',
  `register_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''报备单号'',
  `handle_flag` tinyint(2) NULL DEFAULT 1 COMMENT ''处理标志位1未处理/2已处理/3无需处理'',
  `report_flag` tinyint(2) NULL DEFAULT 1 COMMENT ''上报标志位1未上报/2已上报/3无需上报'',
  `handle_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''处理备注'',
  `create_time` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''创建时间'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for third_party_push_data
-- ----------------------------
DROP TABLE IF EXISTS `third_party_push_data`;
CREATE TABLE `third_party_push_data`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `interface_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `table_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `push_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `market_id` bigint(20) NULL DEFAULT 1 COMMENT ''市场ID'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 36 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for third_party_report_data
-- ----------------------------
DROP TABLE IF EXISTS `third_party_report_data`;
CREATE TABLE `third_party_report_data`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''上报名称'',
  `type` int(11) NULL DEFAULT NULL COMMENT ''上报类型'',
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT ''操作人'',
  `operator_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''操作人ID'',
  `data` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT ''提交数据结果'',
  `success` int(11) NULL DEFAULT NULL COMMENT ''是否成功执行'',
  `msg` varchar(150) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''执行结果'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `market_id` bigint(20) NULL DEFAULT 1 COMMENT ''市场ID'',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 152874 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for third_party_source_data
-- ----------------------------
DROP TABLE IF EXISTS `third_party_source_data`;
CREATE TABLE `third_party_source_data`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''来源名称'',
  `type` int(11) NULL DEFAULT NULL COMMENT ''类型'',
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT ''操作人'',
  `operator_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ''操作人ID'',
  `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT ''数据详情'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `report_flag` tinyint(1) NULL DEFAULT -1 COMMENT ''上报标志位-1未处理/1待上报/2已上报'',
  `market_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 346 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trade_detail
-- ----------------------------
DROP TABLE IF EXISTS `trade_detail`;
CREATE TABLE `trade_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL COMMENT ''分销来源ID'',
  `bill_id` bigint(20) NOT NULL COMMENT ''登记单ID'',
  `checkin_record_id` bigint(20) NULL DEFAULT NULL COMMENT ''进门ID'',
  `checkout_record_id` bigint(20) NULL DEFAULT NULL COMMENT ''进门ID'',
  `checkin_status` int(11) NOT NULL COMMENT ''进门状态'',
  `checkout_status` int(11) NOT NULL COMMENT ''出门状态'',
  `sale_status` int(11) NOT NULL COMMENT ''交易状态'',
  `trade_type` int(11) NOT NULL COMMENT ''交易类型'',
  `buyer_id` bigint(20) NOT NULL COMMENT ''买家ID'',
  `buyer_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `seller_id` bigint(20) NULL DEFAULT NULL COMMENT ''卖家ID'',
  `seller_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `product_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''商品名称'',
  `stock_weight` decimal(10, 3) NOT NULL DEFAULT 0.000 COMMENT ''库存重量'',
  `total_weight` decimal(10, 3) NOT NULL DEFAULT 0.000 COMMENT ''总重量'',
  `weight_unit` int(11) NOT NULL DEFAULT 10 COMMENT ''重量单位'',
  `product_stock_id` bigint(20) NULL DEFAULT NULL COMMENT ''商品库存ID'',
  `trade_request_id` bigint(20) NULL DEFAULT NULL COMMENT ''交易请求ID'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `is_batched` int(11) NOT NULL DEFAULT 0 COMMENT ''是否计算入BatchStock'',
  `batch_no` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''批次号'',
  `parent_batch_no` varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''父批次号'',
  `pushaway_weight` decimal(10, 3) NULL DEFAULT 0.000,
  `soft_weight` decimal(10, 3) NULL DEFAULT 0.000,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `bill_id`(`bill_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23560 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trade_order
-- ----------------------------
DROP TABLE IF EXISTS `trade_order`;
CREATE TABLE `trade_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_status` int(11) NOT NULL COMMENT ''订单状态'',
  `order_type` int(11) NOT NULL COMMENT ''订单类型'',
  `buyer_id` bigint(20) NOT NULL COMMENT ''买家ID'',
  `buyer_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `seller_id` bigint(20) NULL DEFAULT NULL COMMENT ''卖家ID'',
  `seller_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `buyer_market_id` bigint(20) NULL DEFAULT 1 COMMENT ''买家市场ID'',
  `seller_market_id` bigint(20) NULL DEFAULT 1 COMMENT ''卖家市场ID'',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_status`(`order_status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4870 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trade_push_log
-- ----------------------------
DROP TABLE IF EXISTS `trade_push_log`;
CREATE TABLE `trade_push_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `trade_detail_id` bigint(20) NULL DEFAULT NULL COMMENT ''批次主键'',
  `log_type` tinyint(4) NULL DEFAULT NULL COMMENT ''0:下架 1:上架'',
  `product_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''商品名称'',
  `operation_weight` decimal(10, 3) NULL DEFAULT NULL COMMENT ''上下架重量'',
  `order_type` tinyint(4) NULL DEFAULT NULL COMMENT ''0：报备单 10：交易单'',
  `order_id` bigint(20) NULL DEFAULT NULL COMMENT ''单据主键id。报备单id或者交易单id'',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT ''商户 id'',
  `product_stock_id` bigint(20) NULL DEFAULT NULL COMMENT ''库存 id'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT ''创建时间'',
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT ''修改时间'',
  `operation_reason` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `order_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `market_id` bigint(20) NULL DEFAULT 1 COMMENT ''市场ID'',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_TRADE_DETAIL_ID`(`user_id`, `trade_detail_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trade_request
-- ----------------------------
DROP TABLE IF EXISTS `trade_request`;
CREATE TABLE `trade_request`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `buyer_id` bigint(20) NOT NULL COMMENT ''买家ID'',
  `buyer_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `seller_id` bigint(20) NULL DEFAULT NULL COMMENT ''卖家ID'',
  `seller_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `trade_weight` decimal(10, 3) NOT NULL COMMENT ''交易重量'',
  `product_stock_id` bigint(20) NULL DEFAULT NULL COMMENT ''商品库存ID'',
  `trade_order_Id` bigint(20) NOT NULL COMMENT ''订单ID'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `return_status` int(11) NULL DEFAULT NULL COMMENT ''退货状态'',
  `reason` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''原因'',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''编号'',
  `weight_unit` int(11) NOT NULL DEFAULT 10 COMMENT ''重量单位'',
  `spec_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''规格'',
  `product_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''商品名称'',
  `buyer_market_id` bigint(20) NULL DEFAULT 1 COMMENT ''买家市场ID'',
  `seller_market_id` bigint(20) NULL DEFAULT 1 COMMENT ''卖家市场ID'',
  `source_type` tinyint(1) NULL DEFAULT 1 COMMENT ''来源类型 1农溯安/2杭果交易数据'',
  `report_flag` tinyint(1) NULL DEFAULT 1 COMMENT ''是否上报 1上报/2不上报'',
  `batch_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''批号'',
  `origin_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''产地名称'',
  `position_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''仓位码'',
  `position_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''仓位名称'',
  `price` decimal(10, 3) NULL DEFAULT 0.000 COMMENT ''成交价格'',
  `package_number` int(11) NULL DEFAULT 0 COMMENT ''件数'',
  `number` int(11) NULL DEFAULT 0 COMMENT ''成交数量'',
  `amount` decimal(10, 3) NULL DEFAULT 0.000 COMMENT ''成交金额'',
  `pos_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''POS机号'',
  `pay_way` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''支付方式'',
  `total_amount` decimal(10, 3) NULL DEFAULT 0.000 COMMENT ''总金额'',
  `operator` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''营业员'',
  `payer` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''收款员'',
  `pay_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''支付流水号'',
  `third_trade_no` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''第三方交易流水号'',
  `handle_time` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_created`(`created`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5150 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for upstream
-- ----------------------------
DROP TABLE IF EXISTS `upstream`;
CREATE TABLE `upstream`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `upstream_type` int(11) NOT NULL COMMENT ''10个人 20.企业'',
  `id_card` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''身份证号'',
  `telphone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''联系方式'',
  `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''企业(个人)名称'',
  `legal_person` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''法人姓名'',
  `license` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''统一信用代码'',
  `business_license_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''企业营业执照'',
  `manufacturing_license_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''生产许可证'',
  `operation_license_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''经营许可证'',
  `card_no_front_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''身份证照正面'',
  `card_no_back_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''身份证照反面URL'',
  `source_user_id` bigint(20) NULL DEFAULT NULL COMMENT ''复制来源userid'',
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT ''操作人'',
  `operator_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''操作人ID'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `upORdown` tinyint(2) NULL DEFAULT 10 COMMENT ''上游企业10 下游企业20'',
  `market_id` bigint(20) NULL DEFAULT 1 COMMENT ''市场ID'',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `upstream_UN`(`telphone`, `upORdown`, `source_user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1198 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `card_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `addr` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `card_no_front_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''身份证正面'',
  `card_no_back_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''身份证反面'',
  `tally_area_nos` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `business_license_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''营业执照'',
  `sales_city_id` bigint(20) NULL DEFAULT NULL,
  `sales_city_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `state` tinyint(1) NOT NULL DEFAULT 1 COMMENT ''1:启用 0：禁用'',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `yn` tinyint(1) NOT NULL DEFAULT 1 COMMENT ''1:正常 -1：删除'',
  `version` tinyint(4) NOT NULL DEFAULT 0,
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `is_delete` bigint(20) NULL DEFAULT 0 COMMENT ''是否删除'',
  `qr_status` int(11) NULL DEFAULT 0 COMMENT ''二维码状态(默认红色)'',
  `user_type` tinyint(4) NULL DEFAULT NULL COMMENT ''用户类型 个人 10 企业 20'',
  `market_id` bigint(20) NULL DEFAULT 1 COMMENT ''市场ID'',
  `license` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''统一信用代码'',
  `legal_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''法人姓名'',
  `manufacturing_license_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''生产许可证'',
  `operation_license_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''经营许可证'',
  `vocation_type` tinyint(2) NULL DEFAULT NULL COMMENT ''批发 10 农贸 20 团体 30 个人40 餐饮50 配送商 60'',
  `validate_state` tinyint(2) NOT NULL DEFAULT 10 COMMENT ''未实名10 待审核 20  审核未通过 30 审核通过 40'',
  `market_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `business_category_ids` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''经营品类'',
  `business_categories` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''经营品类'',
  `source` tinyint(2) NULL DEFAULT NULL COMMENT ''来源 下游企业：10，注册：20'',
  `pre_qr_status` int(11) NULL DEFAULT NULL COMMENT ''前一次二维码值'',
  `open_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''微信openId'',
  `confirm_date` datetime(0) NULL DEFAULT NULL COMMENT ''提示微信绑定，确认不再弹出日期'',
  `is_push` tinyint(1) NULL DEFAULT -1 COMMENT ''上报标志位，1待上报，-1无需上报'',
  `third_party_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''第三方编码'',
  `credential_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''证件类型'',
  `credential_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''证件名称'',
  `credential_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''证件号码'',
  `credential_url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''证件图片地址'',
  `id_addr` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''身份证地址'',
  `whereis` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''商品去向'',
  `credit_limit` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''授信额度'',
  `effective_date` timestamp(0) NULL DEFAULT NULL COMMENT ''卡有效期'',
  `remark` varchar(400) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''备注'',
  `sex` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''性别'',
  `fixed_telephone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''固定电话'',
  `charge_rate` decimal(5, 2) NULL DEFAULT 0.00 COMMENT ''手续费折扣率'',
  `manger_rate` decimal(5, 2) NULL DEFAULT 0.00 COMMENT ''包装管理费折扣率'',
  `storage_rate` decimal(5, 2) NULL DEFAULT 0.00 COMMENT ''仓储费折扣率'',
  `assess_rate` decimal(5, 2) NULL DEFAULT 0.00 COMMENT ''员工考核折扣率'',
  `approver` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''折扣率批准人'',
  `supplier_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''供应商类型（大客户、临时客户）'',
  `is_active` tinyint(1) NULL DEFAULT -1 COMMENT ''-1去活跃/1活跃'',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_unique_phone`(`phone`, `is_delete`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1013 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_login_history
-- ----------------------------
DROP TABLE IF EXISTS `user_login_history`;
CREATE TABLE `user_login_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `user_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4230 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_plate
-- ----------------------------
DROP TABLE IF EXISTS `user_plate`;
CREATE TABLE `user_plate`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plate` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''车牌号'',
  `user_id` bigint(20) NOT NULL COMMENT ''用户id'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5044 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_qr_history
-- ----------------------------
DROP TABLE IF EXISTS `user_qr_history`;
CREATE TABLE `user_qr_history`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT ''用户(商户)ID'',
  `user_name` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `qr_status` int(11) NOT NULL COMMENT ''二维码状态'',
  `content` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT ''二维码转换信息内容'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `bill_id` bigint(20) NULL DEFAULT NULL COMMENT ''报备单ID'',
  `verify_status` int(11) NOT NULL DEFAULT 0 COMMENT ''查验状态'',
  `is_valid` int(11) NOT NULL DEFAULT 1 COMMENT ''是否有效'',
  `trade_request_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31210 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_qr_item
-- ----------------------------
DROP TABLE IF EXISTS `user_qr_item`;
CREATE TABLE `user_qr_item`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT ''用户(商户)ID'',
  `qr_item_type` int(11) NOT NULL COMMENT ''二维码信息类型'',
  `objects` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `color` int(11) NOT NULL,
  `action` int(11) NOT NULL,
  `has_data` int(11) NOT NULL,
  `valid` int(11) NOT NULL,
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_store
-- ----------------------------
DROP TABLE IF EXISTS `user_store`;
CREATE TABLE `user_store`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `store_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `INDEX_USER_ID`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1161 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_tally_area
-- ----------------------------
DROP TABLE IF EXISTS `user_tally_area`;
CREATE TABLE `user_tally_area`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tally_area_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''理货区号'',
  `user_id` bigint(20) NOT NULL COMMENT ''用户id'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `TALLY_AREA_NO_UNIQUE_INDEX`(`tally_area_no`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for usual_address
-- ----------------------------
DROP TABLE IF EXISTS `usual_address`;
CREATE TABLE `usual_address`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''地址类型'',
  `address_id` bigint(20) NOT NULL COMMENT ''地址id'',
  `address` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''地址'',
  `merged_address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT ''地址全名'',
  `created` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `today_used_count` int(11) NULL DEFAULT 0 COMMENT ''当天使用数量统计'',
  `preday_used_count` int(11) NULL DEFAULT 0 COMMENT ''前一天使用数量统计'',
  `clear_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT ''清理当天使用数量时间'',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `usual_address_unique`(`address_id`, `type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;



-- 初始化脚本
insert into `biz_number` ( `type`, `value`, `memo`, `version`, `modified`, `created`) values('register_bill','2020102900051','订单编码','2294','2019-07-29 20:17:14','2018-11-02 09:45:13');
insert into `biz_number` ( `type`, `value`, `memo`, `version`, `modified`, `created`) values('register_bill_sample_code','2020011900051','登记单采样编号','839','2019-09-17 18:01:56','2019-09-17 18:01:40');
-- 对register_head增加订单编号
insert into `biz_number` ( `type`, `value`, `memo`, `version`, `modified`, `created`) values ('register_head',concat(date_format(now(),'%Y%m%d'),'00000'),'主台账编码','0',now(),now());

insert into `code_generate` ( `type`, `segment`, `seq`, `created`, `modified`, `pattern`, `prefix`) values('SAMPLE_CODE','20200622','5','2020-05-26 14:23:58','2020-06-22 09:22:50','yyyyMMdd','c');
insert into `code_generate` ( `type`, `segment`, `seq`, `created`, `modified`, `pattern`, `prefix`) values('CHECKSHEET_CODE','20200526','0','2020-05-26 14:23:58','2020-05-26 14:23:58','yyyyMMdd','SGJC');
insert into `code_generate` ( `type`, `segment`, `seq`, `created`, `modified`, `pattern`, `prefix`) values('TRADE_REQUEST_CODE','2020102609','2','2020-07-09 10:29:01','2020-10-26 09:56:40','yyyyMMddHH','HZSY');


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
insert into applets_config (app_name,app_id,app_secret,access_token) values('杭州果品溯源小程序','wxa0ff71fe8a3ce984','64742ca2e64752c3dc6fe326807c70fc','init');

-- 初始化全局配置
insert into `sys_config` (`id`, `instructions`, `opt_type`, `opt_category`, `opt_value`) values('1','展示大屏用户基数设定','statisticBaseUser','trade','5');
insert into `sys_config` (`id`, `instructions`, `opt_type`, `opt_category`, `opt_value`) values('2','展示大屏用户基数设定','statisticBaseUser','bill','5');
insert into `sys_config` (`id`, `instructions`, `opt_type`, `opt_category`, `opt_value`) values('4','用户活跃度时效天数','operation_report_limit_day','operation_report_limit_day','0');

-- 市场
insert  into `market`(`id`,`name`,`operator_id`,`operator_name`,`created`,`modified`,`app_id`,`app_secret`,`context_url`,`platform_market_id`) values
(1,'杭水',260,'超级用户','2020-10-23 08:46:41','2020-10-23 08:46:41',NULL,'baa05febaa330aa9ea5ccaa07ed140b24e82387f','http://202.101.190.110:90081',330110800),
(2,'杭果',260,'超级用户','2020-10-23 08:46:41','2020-10-23 08:46:41',NULL,'ac7fd1182026dc0b3ca35b8d168c4f6f187d8e5d','https://pubapi.rongshian.com',33040031);


-- 国外产地默认初始化数据
insert into `countries` (`code`, `cname`) values('ARG','阿根廷');
insert into `countries` (`code`, `cname`) values('AUS','澳大利亚');
insert into `countries` (`code`, `cname`) values('BRA','巴西');
insert into `countries` (`code`, `cname`) values('CHL','智利');
insert into `countries` (`code`, `cname`) values('ECU','厄瓜多尔');
insert into `countries` (`code`, `cname`) values('FRA','法国');
insert into `countries` (`code`, `cname`) values('ISR','以色列');
insert into `countries` (`code`, `cname`) values('ITA','意大利');
insert into `countries` (`code`, `cname`) values('NZL','新西兰');
insert into `countries` (`code`, `cname`) values('PHL','菲律宾');
insert into `countries` (`code`, `cname`) values('ZAF','南非');
insert into `countries` (`code`, `cname`) values('THA','泰国');
insert into `countries` (`code`, `cname`) values('USA','美国');
insert into `countries` (`code`, `cname`) values('VNM','越南');
insert into `countries` (`code`, `cname`) values('JPN','日本');
insert into `countries` (`code`, `cname`) values('MMR','缅甸');

-- 增加杭果的区位号
insert into `tally_area_no` (`number`, `street`, `area`, `created`, `modified`, `market_id`) values('',NULL,'A区',now(),now(),2);
insert into `tally_area_no` (`number`, `street`, `area`, `created`, `modified`, `market_id`) values('',NULL,'B区',now(),now(),2);
insert into `tally_area_no` (`number`, `street`, `area`, `created`, `modified`, `market_id`) values('',NULL,'C区',now(),now(),2);
insert into `tally_area_no` (`number`, `street`, `area`, `created`, `modified`, `market_id`) values('',NULL,'D区',now(),now(),2);
insert into `tally_area_no` (`number`, `street`, `area`, `created`, `modified`, `market_id`) values('',NULL,'E区',now(),now(),2);
insert into `tally_area_no` (`number`, `street`, `area`, `created`, `modified`, `market_id`) values('',NULL,'F区',now(),now(),2);
