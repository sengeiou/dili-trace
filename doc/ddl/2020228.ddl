ALTER TABLE `user_history` DROP COLUMN `user_type`;
ALTER TABLE `register_bill` ADD COLUMN `creation_source`  int default 10 COMMENT '数据创建来源';


ALTER TABLE `usual_address` ADD COLUMN `today_used_count`  int default 0 COMMENT '当天使用数量统计';
ALTER TABLE `usual_address` ADD COLUMN `preday_used_count`  int default 0 COMMENT '前一天使用数量统计';
ALTER TABLE `usual_address` ADD COLUMN `clear_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '清理当天使用数量时间';


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
