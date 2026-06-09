package com.spring.demo.reg_login.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.demo.reg_login.common.Result;
import com.spring.demo.reg_login.service.LikeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    // POST /like → 点赞
    @PostMapping("/like")
    public Result<String> like(@RequestParam Long articleId) {

        likeService.like(articleId);

        return Result.success(
            "点赞成功"
        );

    }

    // DELETE /like → 取消点赞
    @DeleteMapping("/like")
    public Result<String> unlike(@RequestParam Long articleId) {

        likeService.unlike(articleId);

        return Result.success(
            "取消点赞成功"
        );

    }

    // GET /like/count → 点赞数量
    @GetMapping("/like/count")
    public Result<Long> count(@RequestParam Long articleId) {

        return Result.success(
            likeService.count(articleId)
        );

    }

    // GET /like/status → 当前用户是否已点赞
    @GetMapping("/like/status")
    public Result<Boolean> status(@RequestParam Long articleId) {

        return Result.success(
            likeService.hasLiked(articleId)
        );

    }

}
