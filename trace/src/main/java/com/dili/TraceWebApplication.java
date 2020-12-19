package com.dili;

import com.dili.ss.metadata.provider.DatetimeProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.dili.ss.retrofitful.annotation.RestfulScan;

import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 由MyBatis Generator工具自动生成
 */
@SpringBootApplication
//处理事务支持
@EnableAspectJAutoProxy(proxyTargetClass = true)
@MapperScan(basePackages = {"com.dili.trace.dao", "com.dili.ss.dao", "com.dili.ss.uid.dao"})
@ComponentScan(basePackages = {"com.dili.ss", "com.dili.trace", "com.dili.common", "com.dili.commons", "com.dili.uap.sdk"})
@RestfulScan({"com.dili.trace.rpc", "com.dili.uap.sdk.rpc", "com.dili.bpmc.sdk.rpc"})
//@DTOScan({"com.dili.trace","com.dili.ss"})
//@Import(DynamicRoutingDataSourceRegister.class)
@EnableScheduling

@EnableAsync
@EnableFeignClients(basePackages = {"com.dili.assets.sdk.rpc"
        , "com.dili.customer.sdk.rpc"
        , "com.dili.trace.rpc"
        , "com.dili.bpmc.sdk.rpc"})
@Import(FeignClientsConfiguration.class)
@ServletComponentScan
@EnableDiscoveryClient
/**
 * 除了内嵌容器的部署模式，Spring Boot也支持将应用部署至已有的Tomcat容器, 或JBoss, WebLogic等传统Java EE应用服务器。
 * 以Maven为例，首先需要将<packaging>从jar改成war，然后取消spring-boot-maven-plugin，然后修改Application.java
 * 继承SpringBootServletInitializer
 */
public class TraceWebApplication extends SpringBootServletInitializer {
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication sa = new SpringApplication(TraceWebApplication.class);
        sa.addListeners(new ApplicationPidFileWriter());
        ConfigurableApplicationContext ctx = sa.run(args);
//        DatetimeProvider dtp = ctx.getBean(DatetimeProvider.class);
//        System.out.println(dtp);
    }


}
