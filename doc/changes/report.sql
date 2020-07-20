
====================================
/*本日使用食安码户数
（一批报备）*/



	select area,count(1) from (
	 select 
					distinct register_bill.user_id 
					from register_bill 
					where  register_bill.bill_type =10 and  created>='2020-07-18 20:30:00' and created<'2020-07-19 20:30:00'
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
	
	group by area


=====================
/*"本日使用食安码户数
（二批流转）"
*/

select area,count(1) from (

        select 
      
       distinct trade_detail.buyer_id as user_id  
       from trade_detail  left join register_bill  on register_bill.id=trade_detail.bill_id
       
       where trade_detail.parent_id is not null  and register_bill.bill_type =10
       and  trade_detail.created>='2020-07-18 20:30:00'
 and  trade_detail.created<'2020-07-19 20:30:00'  


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

group by area
===========================
/*
审核后生成绿码（一批）
*/


select area,count(1) from (

       select 
        distinct register_bill.user_id 
        from register_bill 
        where  register_bill.created>='2020-07-18 20:30:00' and register_bill.created<'2020-07-19 20:30:00' and bill_type=10  and verify_status in(20)
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

group by area
====================
/*
审核后生成黄码（一批报备）

*/


select area,count(1) from (
 
 select 
        distinct register_bill.user_id 
        from register_bill 
        where  register_bill.created>='2020-07-18 20:30:00' and register_bill.created<'2020-07-19 20:30:00' and bill_type=10 and verify_status in(0,10)
       and user_id  not in(
               select 
         register_bill.user_id 
        from register_bill 
        where  register_bill.created>='2020-07-18 20:30:00' and register_bill.created<'2020-07-19 20:30:00' and bill_type=10  and verify_status in(20)
        )
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

group by area

===================
/*
审核后生成红码（一批）
*/



select area,count(1) from (

 select 
        distinct register_bill.user_id 
        from register_bill 
        where  register_bill.created>='2020-07-18 20:30:00' and register_bill.created<'2020-07-19 20:30:00' and bill_type=10 and verify_status in(30)
       and user_id  not in(
               select 
         register_bill.user_id 
        from register_bill 
        where  register_bill.created>='2020-07-18 20:30:00' and register_bill.created<'2020-07-19 20:30:00' and bill_type=10  and verify_status  in(0,10,20)
        )
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

group by area


=========================






/*
"绿码
（车辆）"	"黄码
（车辆）"	红码	
*/
select count(1), color from (
select register_bill.id,case when verify_status=30 then '红' when verify_status =20 then '绿' else '黄' end  as color  from register_bill where id in(
select distinct bill_id from trade_detail where 
 '2020-07-18 20:30:00'<=created and created < '2020-07-19 20:30:00' 
and trade_detail.checkin_record_id is not null and trade_detail.parent_id is null
)
and bill_type=10)t group by color   

/*
未报备
*/


select distinct register_bill.id from register_bill where id in(
select distinct bill_id from trade_detail where 
 '2020-07-18 20:30:00'<=created and created < '2020-07-19 20:30:00' 
and trade_detail.checkin_record_id is not null  and trade_detail.parent_id is null
)
and bill_type=20
