package com.dili.common.config;

import com.dili.common.entity.LoginSessionContext;
import com.dili.ss.mvc.boot.WebConfig;
import com.dili.ss.mvc.converter.JsonHttpMessageConverter;
import com.dili.trace.interceptor.AddAttributeInterceptor;
import com.dili.trace.interceptor.SessionInterceptor;
import com.dili.trace.interceptor.TanentInterceptor;
import one.util.streamex.StreamEx;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

        int index = StreamEx.of(converters).select(JsonHttpMessageConverter.class)
                .mapToInt(converter -> converters.indexOf(converter)).findFirst().orElse(-1);
        if (index != -1) {
            converters.remove(index);
            converters.add(index, this.mappingJackson2HttpMessageConverter);
        } else {
            converters.add(this.mappingJackson2HttpMessageConverter);
        }
    }

    static class DemoData {
        private Date myDate = new Date();
        private LocalDateTime dateTime = LocalDateTime.now();

        public Date getMyDate() {
            return myDate;
        }

        public void setMyDate(Date myDate) {
            this.myDate = myDate;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }
    }

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        System.out.println(mapper.writeValueAsString(new DemoData()));

    }

}
