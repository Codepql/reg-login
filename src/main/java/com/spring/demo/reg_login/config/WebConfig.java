package com.spring.demo.reg_login.config;

import com.spring.demo.reg_login.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;

// @Configuration: 标记配置类，Spring启动时加载此类中的配置
// 实现WebMvcConfigurer → 自定义Spring MVC的配置（如拦截器、跨域）
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 注册JWT拦截器：拦截所有请求，放行登录/注册/parse
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/**")              // 拦截所有路径
                .excludePathPatterns(                // 放行的路径（不需要Token）
                        "/login",
                        "/register",
                        "/parse",
                        "/upload/*"
                );
    }

    // 跨域配置：允许前端（Vue/React 等）从不同端口/域名访问后端接口
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    // 上传文件
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
