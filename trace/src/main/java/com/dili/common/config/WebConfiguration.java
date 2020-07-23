package com.dili.common.config;

import java.util.concurrent.TimeUnit;

import com.dili.common.entity.LoginSessionContext;
import com.dili.common.interceptor.LoginInterceptor;
import com.dili.common.interceptor.SessionInterceptor;
import com.dili.common.interceptor.SignInterceptor;
import com.diligrp.manage.sdk.session.SessionFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.CacheControl;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
	@Bean
	public SessionFilter sessionFilter() {
		return new SessionFilter();
	}

	@Bean
	public FilterRegistrationBean sessionFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(sessionFilter());
		registration.addUrlPatterns("/*");
		// registration.addInitParameter("confPath", "conf/manage.properties");
		registration.setName("sessionFilter");
		registration.setOrder(1);
		return registration;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/image/**")
				.addResourceLocations("file:" + defaultConfiguration.getImageDirectory()).setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS));
	}

	@Autowired
	private DefaultConfiguration defaultConfiguration;

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public LoginSessionContext sessionContext() {
		return new LoginSessionContext();
	}

	@Bean
	public SessionInterceptor sessionInterceptor() {
		return new SessionInterceptor();
	}

	@Bean
	public LoginInterceptor loginInterceptor() {
		return new LoginInterceptor();
	}

	@Bean
	public SignInterceptor signInterceptor() {
		return new SignInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionInterceptor()).addPathPatterns("/api/**");
		registry.addInterceptor(loginInterceptor()).addPathPatterns("/api/**");
		registry.addInterceptor(signInterceptor()).addPathPatterns("/api/**");
	}

}
