-- 系统要求重量最大值为 99999999，需要增加位数
ALTER TABLE `register_head` MODIFY `piece_weight` decimal(11,3) DEFAULT NULL COMMENT '件重';
ALTER TABLE `register_head` MODIFY `weight` DECIMAL(11,3) NOT NULL DEFAULT '0.000' COMMENT '总重量';
ALTER TABLE `register_head` MODIFY `remain_weight` DECIMAL(11,3) NOT NULL DEFAULT '0.000' COMMENT '剩余重量';

ALTER TABLE `register_bill` MODIFY `piece_weight` decimal(11,3) DEFAULT NULL COMMENT '件重';
ALTER TABLE `register_bill` MODIFY `weight` DECIMAL(11,3) NOT NULL DEFAULT '0.000' COMMENT '总重量';
ALTER TABLE `register_bill` MODIFY `truck_tare_weight` DECIMAL(11,3) DEFAULT NULL;
--ALTER TABLE `register_bill` MODIFY `tare_weight` DECIMAL(11,3) DEFAULT NULL COMMENT '皮重';
