
drop table `user_access_log`;

ALTER TABLE `user_qr_history` ADD COLUMN `bill_id`  bigint(20) NULL COMMENT '报备单ID';
ALTER TABLE `user_qr_history` ADD COLUMN  `verify_status` int(11) not null default 0 COMMENT '查验状态';
ALTER TABLE `user_qr_history` ADD COLUMN  `is_valid` int(11) not null default 1 COMMENT '是否有效';

ALTER TABLE `register_bill` ADD COLUMN `is_deleted` int(11) not null default 0 COMMENT '是否被删除';

ALTER TABLE `register_bill_history` ADD COLUMN `is_deleted` int(11) not null default 0 COMMENT '是否被删除';

ALTER TABLE `register_bill` ADD COLUMN `operation_time` datetime null COMMENT '操作时间';
ALTER TABLE `register_bill_history` ADD COLUMN `operation_time` datetime null COMMENT '操作时间';


ALTER TABLE `checkinout_record` ADD COLUMN  `weight_unit` int(11) NOT NULL default 1 COMMENT '重量单位';
ALTER TABLE `checkinout_record` ADD COLUMN `user_id`  bigint(20) NULL COMMENT '业户ID';


update `register_bill` set operator_name=null,operator_id=null where verify_status=0;
update `register_bill` set operation_time=modified  where verify_status<>0;

update `register_bill_history` set operator_name=null,operator_id=null where verify_status=0;
update `register_bill_history` set operation_time=modified  where verify_status<>0;


update
	checkinout_record t1,
	(
	SELECT
		distinct checkinout_record.id ckid, trade_detail.id as tdid , checkinout_record.trade_detail_id cktdid, trade_detail.checkin_record_id tdckid
	from
		trade_detail
	left join checkinout_record on
		trade_detail.checkin_record_id = checkinout_record.id
	where
		checkinout_record.trade_detail_id <> trade_detail.id
		and trade_detail.parent_id is null ) t2 set
	t1.trade_detail_id = t2.tdid
where
	t1.id = t2.ckid;


update
	checkinout_record ck,
	(
	select
		*
	from
		trade_detail
	where
		parent_id is null)t
		set
	ck.bill_id = t.bill_id
where
	ck.id = t.checkin_record_id
	and ((ck.bill_id <> t.bill_id )or (ck.bill_id is null));


update
	checkinout_record ck,
	 register_bill  r
	set ck.verify_status =r.verify_status ,
	  ck.user_id =r.user_id ,
	  ck.weight_unit =r.weight_unit 
where
	ck.bill_id = r.id ;




CREATE TABLE `third_party_report_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) DEFAULT NULL COMMENT '上报名称',
  `type` int(11) DEFAULT NULL COMMENT '上报类型',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(20) DEFAULT NULL COMMENT '操作人ID',
  `data` text DEFAULT NULL COMMENT '提交数据结果',
  `success`  int(11) DEFAULT NULL COMMENT '是否成功执行',
  `msg` varchar(150) DEFAULT NULL COMMENT '执行结果',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1;
