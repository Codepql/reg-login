package com.spring.demo.reg_login.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.demo.reg_login.common.Result;
import com.spring.demo.reg_login.dto.comment.CommentAddRequest;
import com.spring.demo.reg_login.entity.Comment;
import com.spring.demo.reg_login.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // POST /comment/add → 发表评论
    @PostMapping("/comment/add")
    public Result<String> add(@RequestBody CommentAddRequest request) {

        commentService.add(
            request.getArticleId(),
            request.getContent()
        );

        return Result.success(
            "评论成功"
        );

    }

    // GET /comment/list → 查询文章评论
    @GetMapping("/comment/list")
    public Result<List<Comment>> list(@RequestParam Long articleId) {

        return Result.success(
            commentService.list(articleId)
        );
    }

    // DELETE /comment/delete → 删除评论（仅作者本人）
    @DeleteMapping("/comment/delete")
    public Result<String> delete(@RequestParam Long id) {

        commentService.delete(id);

        return Result.success(
            "删除成功"
        );

    }

}
