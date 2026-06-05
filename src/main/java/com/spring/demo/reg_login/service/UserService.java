package com.spring.demo.reg_login.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.demo.reg_login.common.Result;
import com.spring.demo.reg_login.entity.User;
import com.spring.demo.reg_login.mapper.UserMapper;
import com.spring.demo.reg_login.utils.JwtUtils;

import lombok.RequiredArgsConstructor;

// @Service: 标记Service组件，交给Spring容器管理
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    // BCrypt密码加密器：单向不可逆，每次结果不同（自带随机盐值）
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 注册
    public String register(String username, String password) {

        // 1. 判断用户名是否存在
        User existUser = userMapper.findByUsername(username);

        if (existUser != null) {
            return "用户名已存在~";
        }

        // 2. 创建用户并加密密码后存入数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userMapper.insert(user);

        return "注册成功~";
    }

    // 登录
    public Result<String> login(String username, String password) {

        // 1. 根据用户名查用户
        User user = userMapper.findByUsername(username);

        if (user == null) {
            return Result.error("用户名不存在");
        }

        // 2. 密码校验（明文 vs BCrypt密文）
        boolean result = passwordEncoder.matches(password, user.getPassword());

        if (!result) {
            return Result.error("密码错误");
        }

        // 3. 登录成功，生成JWT Token返回给前端
        String token = JwtUtils.generateToken(user.getUsername());

        return Result.success(token);

    }

}
