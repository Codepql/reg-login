package com.spring.demo.reg_login.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.spring.demo.reg_login.common.Result;
import com.spring.demo.reg_login.dto.LoginRequest;
import com.spring.demo.reg_login.dto.RegisterRequest;
import com.spring.demo.reg_login.entity.User;
import com.spring.demo.reg_login.mapper.UserMapper;
import com.spring.demo.reg_login.service.UserService;
import com.spring.demo.reg_login.utils.ThreadLocalUtil;


import jakarta.servlet.http.HttpServletRequest;
// @RequiredArgsConstructor: Lombok，为final字段自动生成构造方法，Spring通过构造方法注入依赖
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;
    private final StringRedisTemplate redisTemplate;

    // GET /user?id=1 → 根据ID查用户
    @GetMapping("/user")
    public String getUser(@RequestParam Long id) {
        User user = userMapper.findById(id);
        if (user == null) {
            return "当前用户不存在~";
        }
        return user.toString();
    }

    // POST /register → 用户注册
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return userService.register(
                request.getUsername(),
                request.getPassword()
        );
    }

    // POST /login → 用户登录，返回JWT Token
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest request) {
        return userService.login(
                request.getUsername(),
                request.getPassword()
        );
    }

    // GET /me → 获取当前登录用户（Token通过拦截器解析后存入request，这里直接取出）
    @GetMapping("/me")
    public String me(HttpServletRequest request) {
        return (String) request.getAttribute("username");
    }

    // GET /logout → 用户登出，删除Redis中的Token
    @PostMapping("/logout")
    public Result<String> logout() {

        String username = ThreadLocalUtil.get();

        redisTemplate.delete(
                "login:" + username
        );

        return Result.success(
                "退出成功"
        );
    }

}
