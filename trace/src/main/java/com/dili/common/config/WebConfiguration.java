package com.dili.common.config;

import com.dili.common.entity.LoginSessionContext;
import com.dili.ss.mvc.boot.WebConfig;
import com.dili.ss.mvc.converter.JsonHttpMessageConverter;
import com.dili.trace.interceptor.AddAttributeInterceptor;
import com.dili.trace.interceptor.SessionInterceptor;
import com.dili.trace.interceptor.TanentInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Configuration
public class WebConfiguration extends WebConfig {
    @Autowired
    MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter;

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
    public AddAttributeInterceptor addAttributeInterceptor() {
        return new AddAttributeInterceptor();
    }

    @Bean
    public TanentInterceptor tanentInterceptor() {
        return new TanentInterceptor();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tanentInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(sessionInterceptor()).addPathPatterns("/api/**");
        registry.addInterceptor(addAttributeInterceptor()).addPathPatterns("/**");

    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        Iterator it = converters.iterator();
        int index = -1;
        int tmp = 0;

        while (it.hasNext()) {
            Object obj = it.next();
            if (obj instanceof JsonHttpMessageConverter) {
                it.remove();
                index = tmp;
            }

            if (index == -1) {
                ++tmp;
            }
        }

        if (index != -1) {
            converters.add(index, this.mappingJackson2HttpMessageConverter);
        } else {
            converters.add(this.mappingJackson2HttpMessageConverter);
        }
    }

}
