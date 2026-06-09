package com.spring.demo.reg_login.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Category {
    private Long id;

    // 分类名称
    private String categoryName;

    // 创建人
    private String createUser;

    // 创建时间
    private LocalDateTime createTime;
}
