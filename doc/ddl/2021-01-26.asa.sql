DROP TABLE IF EXISTS category;

CREATE TABLE `category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上一级ID',
  `name` varchar(20) DEFAULT NULL COMMENT '名称',
  `full_name` varchar(20) DEFAULT NULL COMMENT '全名',
  `level` int(11) NOT NULL DEFAULT '1' COMMENT '层级',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `market_id` varchar(20) DEFAULT NULL,
  `code` varchar(20) DEFAULT NULL COMMENT '第三方编码',
  `is_show` varchar(1) DEFAULT '1' COMMENT '登记显示,1显示,2不显示',
  `type` tinyint(1) DEFAULT '1' COMMENT '商品类型,1杭水，2检测商品',
  `specification` varchar(20) DEFAULT NULL COMMENT '商品规格名',
  `parent_code` varchar(20) DEFAULT NULL COMMENT '父级第三方编码',
  `uap_id` bigint(20) DEFAULT NULL COMMENT '调用uap获取商品id',
  `uap_parent_id` bigint(20) DEFAULT NULL COMMENT '调用uap获取parentid(处理后)',
  `old_uap_parent_id` bigint(20) DEFAULT NULL COMMENT '调用uap获取商品parentid',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;