 select * from (
 select 
        register_bill.id as bill_id
        ,register_bill.code as bill_code
        ,register_bill.name as user_name
        ,register_bill.plate 
        ,register_bill.brand_name
        ,register_bill.preserve_type 
        ,register_bill.origin_name 
        ,register_bill.verify_status  
        ,register_bill.product_name
        ,register_bill.weight
        ,register_bill.weight_unit 
        ,register_bill.created AS bill_created
        ,u.id as user_id 
        ,u.phone
        ,u.tally_area_nos
        ,u.legal_person
        ,checkinout_record.created AS 'checkin_created'
        ,checkinout_record.status AS checkin_status
        from register_bill left join trade_detail on register_bill.id=trade_detail.bill_id
        left join checkinout_record on checkinout_record.id=trade_detail.checkin_record_id
        left join `user` u on register_bill.user_id=u.id
        where trade_detail.parent_id is null
 ) bill left join r_user_tally_area  on  bill.user_id=r_user_tally_area.user_id 
 left join tally_area_no on tally_area_no.id =r_user_tally_area.tally_area_no_id 


==================================================


select * from (
 select 
        register_bill.id as bill_id
        ,register_bill.code as bill_code
        ,register_bill.name as user_name
        ,register_bill.plate 
        ,register_bill.brand_name
        ,register_bill.preserve_type 
        ,register_bill.origin_name 
        ,register_bill.verify_status  
        ,register_bill.product_name
        ,register_bill.weight
        ,register_bill.weight_unit 
        ,register_bill.created AS bill_created
        ,u.id as user_id 
        ,u.phone
        ,u.tally_area_nos
        ,u.legal_person
        ,checkinout_record.created AS 'checkin_created'
        ,checkinout_record.status AS checkin_status
        from register_bill left join trade_detail on register_bill.id=trade_detail.bill_id
        left join checkinout_record on checkinout_record.id=trade_detail.checkin_record_id
        left join `user` u on register_bill.user_id=u.id
        where trade_detail.parent_id is null
 ) bill 
where ( (
bill_created>'2020-07-15 20:00:00' and bill_created<='2020-07-16 20:00:00'
)
or
(
checkin_created>'2020-07-15 20:00:00' and checkin_created<='2020-07-16 20:00:00'
)
)
and verify_status=20
and bill_id in(
	select bill_id from trade_detail
)
========================================

select id,
case when (
tally_area_nos like '%-6-%'
or tally_area_nos like '%-7-%'
or tally_area_nos like '%7街%'
or tally_area_nos like '%-8-%'
or tally_area_nos like '%-9-%'
or tally_area_nos like '%-XQ-%'
or tally_area_nos like '%虾区%'
)then 'A1'
 when (
tally_area_nos like '%-3-%'
or tally_area_nos like '%3街%'
or tally_area_nos like '%-5-%'
or tally_area_nos like '%5街%'
or tally_area_nos like '%-18-%'
or tally_area_nos like '%东大街%'
)then 'A2'
 when (
tally_area_nos like '%-11-%'
or tally_area_nos like '%-12-%'
or tally_area_nos like '%-13-%'
or tally_area_nos like '%HY%'
)then 'B1'
when (
tally_area_nos like '%-10-%'
or tally_area_nos like '%BX%'
or tally_area_nos like '%SZ%'

)then 'B2'

else 'Others'
end

from `user` u

==============================

select * from (
 select 
        register_bill.id as bill_id
        ,register_bill.code as bill_code
        ,register_bill.name as user_name
        ,register_bill.plate 
        ,register_bill.brand_name
        ,register_bill.preserve_type 
        ,register_bill.origin_name 
        ,register_bill.verify_status  
        ,register_bill.product_name
        ,register_bill.weight
        ,register_bill.weight_unit 
        ,register_bill.created AS bill_created
        ,u.id as user_id 
        ,u.phone
        ,u.tally_area_nos
        ,u.legal_person
        ,checkinout_record.created AS 'checkin_created'
        ,checkinout_record.status AS checkin_status
        from register_bill left join trade_detail on register_bill.id=trade_detail.bill_id
        left join checkinout_record on checkinout_record.id=trade_detail.checkin_record_id
        left join `user` u on register_bill.user_id=u.id
        where trade_detail.parent_id is null
 ) bill left join (


select id as user_id,
case when (
tally_area_nos like '%-6-%'
or tally_area_nos like '%-7-%'
or tally_area_nos like '%7街%'
or tally_area_nos like '%-8-%'
or tally_area_nos like '%-9-%'
or tally_area_nos like '%-XQ-%'
or tally_area_nos like '%虾区%'
)then 'A1'
 when (
tally_area_nos like '%-3-%'
or tally_area_nos like '%3街%'
or tally_area_nos like '%-5-%'
or tally_area_nos like '%5街%'
or tally_area_nos like '%-18-%'
or tally_area_nos like '%东大街%'
)then 'A2'
 when (
tally_area_nos like '%-11-%'
or tally_area_nos like '%-12-%'
or tally_area_nos like '%-13-%'
or tally_area_nos like '%HY%'
)then 'B1'
when (
tally_area_nos like '%-10-%'
or tally_area_nos like '%BX%'
or tally_area_nos like '%SZ%'

)then 'B2'

else 'Others'
end as area

from `user` u

 )tu on bill.user_id=tu.user_id
where  bill_created>'2020-07-15 20:00:00' and bill_created<='2020-07-16 20:00:00'
and verify_status=20
and bill_id in( select bill_id from trade_detail where parent_id is not null )


====================================
/*本日使用食安码户数
（一批报备）*/


select area,count(1) from (
 select 
        register_bill.id as bill_id
        ,register_bill.code as bill_code
        ,register_bill.name as user_name
        ,register_bill.plate 
        ,register_bill.brand_name
        ,register_bill.preserve_type 
        ,register_bill.origin_name 
        ,register_bill.verify_status  
        ,register_bill.product_name
        ,register_bill.weight
        ,register_bill.weight_unit 
        ,register_bill.created AS bill_created
        ,u.id as user_id 
        ,u.phone
        ,u.tally_area_nos
        ,u.legal_person
        ,checkinout_record.created AS 'checkin_created'
        ,checkinout_record.status AS checkin_status
        from register_bill left join trade_detail on register_bill.id=trade_detail.bill_id
        left join checkinout_record on checkinout_record.id=trade_detail.checkin_record_id
        left join `user` u on register_bill.user_id=u.id
        where trade_detail.parent_id is null
 ) bill left join (


select id as user_id,
case when (
tally_area_nos like '%-6-%'
or tally_area_nos like '%-7-%'
or tally_area_nos like '%7街%'
or tally_area_nos like '%-8-%'
or tally_area_nos like '%-9-%'
or tally_area_nos like '%-XQ-%'
or tally_area_nos like '%虾区%'
)then 'A1'
 when (
tally_area_nos like '%-3-%'
or tally_area_nos like '%3街%'
or tally_area_nos like '%-5-%'
or tally_area_nos like '%5街%'
or tally_area_nos like '%-18-%'
or tally_area_nos like '%东大街%'
)then 'A2'
 when (
tally_area_nos like '%-11-%'
or tally_area_nos like '%-12-%'
or tally_area_nos like '%-13-%'
or tally_area_nos like '%HY%'
)then 'B1'
when (
tally_area_nos like '%-10-%'
or tally_area_nos like '%BX%'
or tally_area_nos like '%SZ%'

)then 'B2'

else 'Others'
end as area

from `user` u

 )tu on bill.user_id=tu.user_id
where  bill_created>'2020-07-15 20:00:00' and bill_created<='2020-07-16 20:00:00'
and verify_status=20
group by area

=====================
/*"本日使用食安码户数
（二批流转）"
*/

select area,count(1) from (
 select 
        trade_detail.buyer_id as user_id 
 ,trade_detail.created AS bill_created
  ,trade_detail.parent_id
        from trade_detail   
 ) bill left join (


select id as user_id,
case when (
tally_area_nos like '%-6-%'
or tally_area_nos like '%-7-%'
or tally_area_nos like '%7街%'
or tally_area_nos like '%-8-%'
or tally_area_nos like '%-9-%'
or tally_area_nos like '%-XQ-%'
or tally_area_nos like '%虾区%'
)then 'A1'
 when (
tally_area_nos like '%-3-%'
or tally_area_nos like '%3街%'
or tally_area_nos like '%-5-%'
or tally_area_nos like '%5街%'
or tally_area_nos like '%-18-%'
or tally_area_nos like '%东大街%'
)then 'A2'
 when (
tally_area_nos like '%-11-%'
or tally_area_nos like '%-12-%'
or tally_area_nos like '%-13-%'
or tally_area_nos like '%HY%'
)then 'B1'
when (
tally_area_nos like '%-10-%'
or tally_area_nos like '%BX%'
or tally_area_nos like '%SZ%'

)then 'B2'

else 'Others'
end as area

from `user` u

 )tu on bill.user_id=tu.user_id
where  bill_created>'2020-07-15 20:00:00' and bill_created<='2020-07-16 20:00:00'
and bill.parent_id is not null
group by area

===========================
/*
审核后生成绿码（一批）
*/


select area,count(1) from (
 select 
        register_bill.created AS bill_created
				,register_bill.verify_status  
        ,u.id as user_id 
        from register_bill 
        left join `user` u on register_bill.user_id=u.id
 ) bill left join (


select id as user_id,
case when (
tally_area_nos like '%-6-%'
or tally_area_nos like '%-7-%'
or tally_area_nos like '%7街%'
or tally_area_nos like '%-8-%'
or tally_area_nos like '%-9-%'
or tally_area_nos like '%-XQ-%'
or tally_area_nos like '%虾区%'
)then 'A1'
 when (
tally_area_nos like '%-3-%'
or tally_area_nos like '%3街%'
or tally_area_nos like '%-5-%'
or tally_area_nos like '%5街%'
or tally_area_nos like '%-18-%'
or tally_area_nos like '%东大街%'
)then 'A2'
 when (
tally_area_nos like '%-11-%'
or tally_area_nos like '%-12-%'
or tally_area_nos like '%-13-%'
or tally_area_nos like '%HY%'
)then 'B1'
when (
tally_area_nos like '%-10-%'
or tally_area_nos like '%BX%'
or tally_area_nos like '%SZ%'

)then 'B2'

else 'Others'
end as area

from `user` u

 )tu on bill.user_id=tu.user_id
where  bill_created>'2020-07-15 20:00:00' and bill_created<='2020-07-16 20:00:00'
and verify_status=20
group by area

====================
/*
审核后生成黄码（一批报备）

*/


select area,count(1) from (
 select 
				register_bill.id AS bill_id
        ,register_bill.created AS bill_created
				,register_bill.verify_status  
        ,u.id as user_id 
        from register_bill 
        left join `user` u on register_bill.user_id=u.id
 ) bill left join (


select id as user_id,
case when (
tally_area_nos like '%-6-%'
or tally_area_nos like '%-7-%'
or tally_area_nos like '%7街%'
or tally_area_nos like '%-8-%'
or tally_area_nos like '%-9-%'
or tally_area_nos like '%-XQ-%'
or tally_area_nos like '%虾区%'
)then 'A1'
 when (
tally_area_nos like '%-3-%'
or tally_area_nos like '%3街%'
or tally_area_nos like '%-5-%'
or tally_area_nos like '%5街%'
or tally_area_nos like '%-18-%'
or tally_area_nos like '%东大街%'
)then 'A2'
 when (
tally_area_nos like '%-11-%'
or tally_area_nos like '%-12-%'
or tally_area_nos like '%-13-%'
or tally_area_nos like '%HY%'
)then 'B1'
when (
tally_area_nos like '%-10-%'
or tally_area_nos like '%BX%'
or tally_area_nos like '%SZ%'

)then 'B2'

else 'Others'
end as area

from `user` u

 )tu on bill.user_id=tu.user_id
where  bill_created>'2020-07-15 20:00:00' and bill_created<='2020-07-16 20:00:00'
and bill_id not in(
       select id from register_bill where verify_status=20 and created>'2020-07-15 20:00:00' and created<='2020-07-16 20:00:00'
)
and verify_status in(0,10)
group by area

===================
/*
审核后生成红码（一批）
*/



select area,count(1) from (
 select 
				register_bill.id AS bill_id
        ,register_bill.created AS bill_created
				,register_bill.verify_status  
        ,u.id as user_id 
        from register_bill 
        left join `user` u on register_bill.user_id=u.id
 ) bill left join (


select id as user_id,
case when (
tally_area_nos like '%-6-%'
or tally_area_nos like '%-7-%'
or tally_area_nos like '%7街%'
or tally_area_nos like '%-8-%'
or tally_area_nos like '%-9-%'
or tally_area_nos like '%-XQ-%'
or tally_area_nos like '%虾区%'
)then 'A1'
 when (
tally_area_nos like '%-3-%'
or tally_area_nos like '%3街%'
or tally_area_nos like '%-5-%'
or tally_area_nos like '%5街%'
or tally_area_nos like '%-18-%'
or tally_area_nos like '%东大街%'
)then 'A2'
 when (
tally_area_nos like '%-11-%'
or tally_area_nos like '%-12-%'
or tally_area_nos like '%-13-%'
or tally_area_nos like '%HY%'
)then 'B1'
when (
tally_area_nos like '%-10-%'
or tally_area_nos like '%BX%'
or tally_area_nos like '%SZ%'

)then 'B2'

else 'Others'
end as area

from `user` u

 )tu on bill.user_id=tu.user_id
where  bill_created>'2020-07-15 20:00:00' and bill_created<='2020-07-16 20:00:00'
and bill_id not in(
       select id from register_bill where verify_status in(0,10,20) and created>'2020-07-15 20:00:00' and created<='2020-07-16 20:00:00'
)
and verify_status =30
group by area


=========================
/*
"绿码
（车辆）"	"黄码
（车辆）"	红码	
*/
select count(1), color from (

select distinct  register_bill.id,case when verify_status=30 then '红' when verify_status =20 then '绿' else '黄' end  as color from register_bill
 left join  trade_detail  on register_bill.id=trade_detail.id
where trade_detail.parent_id is null
and register_bill.bill_type=10
and '2020-07-15 20:00:00'<=trade_detail.created and trade_detail.created < '2020-07-16 20:00:00'
)t group by color

/*
未报备
*/

select count(distinct register_bill.id) from register_bill
 left join  trade_detail  on register_bill.id=trade_detail.id
where trade_detail.parent_id is null
and register_bill.bill_type=10
and '2020-07-15 20:00:00'<=trade_detail.created and trade_detail.created < '2020-07-16 20:00:00'