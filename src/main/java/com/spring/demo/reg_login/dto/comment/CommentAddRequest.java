package com.spring.demo.reg_login.dto.comment;

import lombok.Data;

// DTO: 新增评论请求
@Data
public class CommentAddRequest {

    private Long articleId;

    private String content;

}
