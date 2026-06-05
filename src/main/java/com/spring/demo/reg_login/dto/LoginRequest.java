package com.spring.demo.reg_login.dto;

import lombok.Data;

// DTO: 登录请求
@Data
public class LoginRequest {
    private String username;

    private String password;
}
