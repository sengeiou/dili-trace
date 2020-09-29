DELETE FROM sys_config WHERE opt_type='operation_report_limit_day'
AND opt_category = 'operation_report_limit_day';
INSERT INTO `sys_config` ( `instructions`, `opt_type`, `opt_category`, `opt_value`) 
VALUES('用户活跃度时效天数','operation_report_limit_day','operation_report_limit_day','0');
