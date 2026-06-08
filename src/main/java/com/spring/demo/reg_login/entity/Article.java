package com.spring.demo.reg_login.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Article {
    private Long id;

    private String title;

    private String content;

    private String author;

    private String coverImg;

    private LocalDateTime createTime;
}
