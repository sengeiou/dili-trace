ALTER TABLE `register_bill` MODIFY COLUMN `handle_result`  varchar(10000);
ALTER TABLE `register_bill` MODIFY COLUMN `handle_result_url`  varchar(4000);

ALTER TABLE `register_bill` MODIFY COLUMN `origin_certifiy_url`  varchar(4000);
ALTER TABLE `register_bill` MODIFY COLUMN `detect_report_url`  varchar(4000);