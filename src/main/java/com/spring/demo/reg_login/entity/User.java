package com.spring.demo.reg_login.entity;

import lombok.Data;

import java.time.LocalDateTime;

// Entity: 实体类，和数据库user表一一对应（类名→表名，属性→字段）
@Data
public class User {
    private Long id;                    // 主键（自增）

    private String username;            // 用户名

    private String password;            // 密码（存BCrypt加密后的密文）

    private LocalDateTime createTime;   // 创建时间
}
