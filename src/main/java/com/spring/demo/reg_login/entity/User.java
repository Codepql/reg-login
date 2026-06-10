package com.spring.demo.reg_login.entity;

import java.time.LocalDateTime;

import lombok.Data;

// Entity: 实体类，和数据库user表一一对应（类名→表名，属性→字段）
@Data
public class User {
    private Long id;                    // 主键（自增）

    private String username;            // 用户名

    private String password;          // 密码（存BCrypt加密后的密文）

    private LocalDateTime createTime;   // 创建时间

    private String role;              // 角色（普通用户：USER，管理员：ADMIN）

    private Integer status;         // 状态（0：正常，1：封禁，管理员可以封禁用户）
}
