package com.dili;

import com.dili.ss.datasource.aop.DynamicRoutingDataSourceRegister;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import com.dili.ss.retrofitful.annotation.RestfulScan;

/**
 * 由MyBatis Generator工具自动生成
 */
@SpringBootApplication
//处理事务支持
@EnableAspectJAutoProxy(proxyTargetClass = true)
@MapperScan(basePackages = {"com.dili.trace.dao","com.dili.trace.etrade.dao", "com.dili.ss.dao", "com.dili.ss.uid.dao"})
@ComponentScan(basePackages={"com.dili.ss","com.dili.trace","com.dili.trace.etrade","com.dili.common"})
@RestfulScan({"com.dili.trace.rpc"})
@Import(DynamicRoutingDataSourceRegister.class)
@EnableScheduling
/**
 * 除了内嵌容器的部署模式，Spring Boot也支持将应用部署至已有的Tomcat容器, 或JBoss, WebLogic等传统Java EE应用服务器。
 * 以Maven为例，首先需要将<packaging>从jar改成war，然后取消spring-boot-maven-plugin，然后修改Application.java
 * 继承SpringBootServletInitializer
 */
public class TraceWebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(TraceWebApplication.class, args);
    }


}
