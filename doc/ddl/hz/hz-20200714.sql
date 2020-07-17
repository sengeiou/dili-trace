
alter table `batch_stock` rename to `product_stock`;
alter table `trade_detail` change `batch_stock_id`  `product_stock_id` bigint(20) NULL COMMENT '商品库存ID';
alter table `trade_request` change `batch_stock_id`  `product_stock_id` bigint(20) NULL COMMENT '商品库存ID';


ALTER TABLE `checkinout_record` ADD COLUMN  `verify_status` int(11) not null default 0 COMMENT '查验状态';
ALTER TABLE `checkinout_record` ADD COLUMN  `bill_type` int(11)  NOT NULL  default 10 COMMENT '报备类型';


ALTER TABLE `user` ADD `pre_qr_status`int(11) NULL COMMENT '前一次二维码值';
update `user` set pre_qr_status=qr_status;

ALTER TABLE `trade_detail` ADD `batch_no` varchar(60) NULL COMMENT '批次号';
ALTER TABLE `trade_detail` ADD `parent_batch_no` varchar(60) NULL COMMENT '父批次号';

update `trade_detail`  set `batch_no`=CONCAT(buyer_name," ",buyer_name);
update `trade_detail`  set `parent_batch_no`=(select CONCAT(name," ",created) from register_bill where trade_detail.bill_id=register_bill.id ) 
    where trade_type=0;

update `trade_detail` t1,`trade_detail` t2 set t1.parent_batch_no=t2.batch_no 
    where t1.parent_id =t2.id and t1.trade_type=10;



CREATE TABLE `user_login_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `user_name` varchar(30)  NULL COMMENT '姓名',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;


DROP TABLE `user_history`;



CREATE TABLE `user_qr_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20)  NULL COMMENT '用户(商户)ID',
  `user_name` varchar(30)  NULL COMMENT '用户(商户)名称',
  `qr_status` int(11) NOT NULL  COMMENT '二维码状态',
  `content` varchar(200) NULL COMMENT '二维码转换信息内容',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `user_access_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20)  NULL COMMENT '用户(商户)ID',
  `user_name` varchar(30)  NULL COMMENT '用户(商户)名称',
  `login_type` int(11) NULL COMMENT '用户类型',
  `url` varchar(400) NOT NULL  COMMENT 'url',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `tally_area_no` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `number` varchar(50)  NULL COMMENT '摊位号',
  `street` varchar(30)  NULL COMMENT '街区号',
  `area` varchar(30) NOT NULL  COMMENT '区域',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `r_user_tally_area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tally_area_no_id` bigint(20)  NULL COMMENT '摊位号ID',
  `user_id` bigint(20)  NULL COMMENT '用户ID',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
