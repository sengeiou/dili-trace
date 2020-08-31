DROP TABLE IF EXISTS third_party_push_data;
CREATE TABLE `third_party_push_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `interface_name` varchar(50) NOT NULL,
  `table_name` varchar(50) NOT NULL,
  `push_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
