/*
SQLyog Ultimate
MySQL - 5.7.25 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;
CREATE TABLE `countries` (
    `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
    `code` char(3) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '地区代码',
    `cname` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE,
    KEY `countries_code_index` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

insert into `countries` (`code`, `cname`) values('ARG','阿根廷');
insert into `countries` (`code`, `cname`) values('AUS','澳大利亚');
insert into `countries` (`code`, `cname`) values('BRA','巴西');
insert into `countries` (`code`, `cname`) values('CHL','智利');
insert into `countries` (`code`, `cname`) values('ECU','厄瓜多尔');
insert into `countries` (`code`, `cname`) values('FRA','法国');
insert into `countries` (`code`, `cname`) values('ISR','以色列');
insert into `countries` (`code`, `cname`) values('ITA','意大利');
insert into `countries` (`code`, `cname`) values('NZL','新西兰');
insert into `countries` (`code`, `cname`) values('PHL','菲律宾');
insert into `countries` (`code`, `cname`) values('ZAF','南非');
insert into `countries` (`code`, `cname`) values('THA','泰国');
insert into `countries` (`code`, `cname`) values('USA','美国');
insert into `countries` (`code`, `cname`) values('VNM','越南');
insert into `countries` (`code`, `cname`) values('JPN','日本');
insert into `countries` (`code`, `cname`) values('MMR','缅甸');