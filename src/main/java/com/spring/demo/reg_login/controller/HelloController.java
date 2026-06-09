package com.spring.demo.reg_login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController: REST控制器（=@Controller + @ResponseBody），方法返回值直接写入HTTP响应体
@RestController
public class HelloController {

    // GET /hello → 测试接口，无需登录，用于健康检查
    @GetMapping("/hello")
    public String hello() {
        return "hello spring boot";
    }

    // GET /info → 需要登录才能访问（被JwtAuthFilter拦截校验Token）
    @GetMapping("/info")
    public String info() {
        return "当前登录用户可以访问";
    }

}
