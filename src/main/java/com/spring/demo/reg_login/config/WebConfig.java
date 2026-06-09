package com.spring.demo.reg_login.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;

// @Configuration: 标记配置类，Spring启动时加载此类中的配置
// 实现WebMvcConfigurer → 自定义Spring MVC的配置（跨域、静态资源）
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 跨域配置：允许前端（Vue/React 等）从不同端口/域名访问后端接口
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    // 上传文件静态资源映射：/upload/** → 本地目录
    @Value("${file.upload-path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {

        registry.addResourceHandler(
                "/upload/**"
        ).addResourceLocations(
                "file:" + uploadPath
        );

    }

}
