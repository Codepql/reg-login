package com.spring.demo.reg_login.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Comment {
    private Long id;

    private Long articleId;

    private String content;

    private String createUser;

    private LocalDateTime createTime;
}
