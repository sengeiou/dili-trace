package com.dili.common.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.github.gavlyukovskiy.boot.jdbc.decorator.dsproxy.ProxyDataSourceBuilderConfigurer;

import net.ttddyy.dsproxy.support.ProxyDataSource;

@Configuration
public class ProxyDataSourceConfig {

	@Bean
	@Primary
	ProxyDataSource proxyDatasource(@Autowired DataSource ds) {

		return new ProxyDataSource(ds);
	}
}
