package com.spring.demo.reg_login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// @SpringBootApplication: 启动类注解（=@Configuration + @EnableAutoConfiguration + @ComponentScan 三合一）
@SpringBootApplication
@EnableScheduling
public class RegLoginApplication {

    public static void main(String[] args) {
        // 启动Spring Boot，创建容器、启动内嵌Tomcat、扫描并加载所有Bean
        SpringApplication.run(RegLoginApplication.class, args);
    }

}
