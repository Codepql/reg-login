package com.spring.demo.reg_login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.demo.reg_login.utils.JwtUtils;

// @RestController: REST控制器（=@Controller + @ResponseBody），方法返回值直接写入HTTP响应体
@RestController
public class HelloController {

    // GET /hello → 测试接口，无需登录
    @GetMapping("/hello")
    public String hello() {
        return "hello spring boot";
    }

    // GET /parse → 解析JWT Token（测试接口，不校验登录状态，方便前端调试）
    @GetMapping("/parse")
    public String parse(String token) {
        return JwtUtils.parseToken(token);
    }

    // GET /info → 需要登录才能访问（被JwtInterceptor拦截校验Token）
    @GetMapping("/info")
    public String info() {
        return "当前登录用户可以访问";
    }

}
