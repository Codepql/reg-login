package com.spring.demo.reg_login.dto.article;

import lombok.Data;

@Data
public class ArticleUpdateRequest {
    private Long id;

    private String title;

    private String content;
}
