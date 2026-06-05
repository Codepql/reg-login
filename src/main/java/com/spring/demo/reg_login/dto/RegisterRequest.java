package com.spring.demo.reg_login.dto;

import lombok.Data;

// DTO: 数据传输对象，专门接收前端发来的JSON数据（只需要username+password，不需要id和createTime）
@Data
public class RegisterRequest {
    private String username;

    private String password;
}
