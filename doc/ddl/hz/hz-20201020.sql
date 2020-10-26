DROP TABLE IF EXISTS register_head;
CREATE TABLE `register_head` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '表主键',
  `code` varchar(20) NOT NULL COMMENT '主台账编号',
  `bill_type` int(11) NOT NULL COMMENT '单据类型。10-正常进场 20-补单 30-外冷分批进场。',
  `user_id` bigint(20) NOT NULL COMMENT '业户ID',
  `name` varchar(50) DEFAULT NULL COMMENT '业户姓名',
  `id_card_no` varchar(20) DEFAULT NULL COMMENT '业户身份证号',
  `third_party_code` varchar(20) DEFAULT NULL COMMENT '经营户卡号',
  `addr` varchar(50) DEFAULT NULL COMMENT '业户地址',
  `phone` varchar(20) DEFAULT NULL COMMENT '业户手机',
  `plate` varchar(15) DEFAULT NULL COMMENT '车牌号',
  `product_id` bigint(20) NOT NULL COMMENT '商品id',
  `product_name` varchar(20) DEFAULT NULL COMMENT '商品名称',
  `measure_type` tinyint(2) NOT NULL COMMENT '计量类型。10-计件 20-计重。默认计件。',
  `piece_num` decimal(10,3) DEFAULT NULL COMMENT '件数',
  `piece_weight` decimal(10,3) DEFAULT NULL COMMENT '件重',
  `weight` decimal(10,3) NOT NULL COMMENT '总重量',
  `remain_weight` decimal(10,3) NOT NULL COMMENT '剩余重量',
  `weight_unit` int(11) NOT NULL COMMENT '重量单位。1-斤 2-公斤。默认1。',
  `upstream_id` bigint(20) NOT NULL COMMENT '上游id',
  `spec_name` varchar(20) DEFAULT NULL COMMENT '规格',
  `origin_id`  bigint(20) DEFAULT NULL COMMENT '产地id',
  `origin_name` varchar(50) DEFAULT NULL COMMENT '产地',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌id',
  `brand_name` varchar(50) DEFAULT NULL COMMENT '品牌名称',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_user` varchar(50) DEFAULT NULL COMMENT '修改人',
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_deleted` tinyint(2) DEFAULT NULL COMMENT '是否作废。0-否 1-是',
  `delete_user` varchar(50) DEFAULT NULL COMMENT '作废人',
  `delete_time` timestamp DEFAULT NULL COMMENT '作废时间',
  `version` tinyint(4) NOT NULL COMMENT '版本号',
  `reason` varchar(100) DEFAULT NULL COMMENT '原因',
  `active` tinyint(2) DEFAULT NULL COMMENT '是否启用。0-否 1-是',
  `market_id` bigint(20) DEFAULT NULL COMMENT '市场ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
