package com.dili.common.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 配置过滤器，解决跨域问题
 * @author asa.lee
 */
@Configuration
public class CorsConfig 
{
	 private CorsConfiguration buildConfig()
	 {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
         //允许任何域名使用
        corsConfiguration.addAllowedOrigin("*");
         //允许任何头
        corsConfiguration.addAllowedHeader("*");
         //允许任何方法（post、get等）
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(3600L);
        return corsConfiguration;
    }
 
    @Bean
    public CorsFilter corsFilter()
    {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(source);
    }
    
}