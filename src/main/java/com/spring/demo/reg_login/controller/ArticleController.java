package com.spring.demo.reg_login.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.demo.reg_login.common.PageResult;
import com.spring.demo.reg_login.common.Result;
import com.spring.demo.reg_login.dto.article.ArticleDeleteRequest;
import com.spring.demo.reg_login.dto.article.ArticleRequest;
import com.spring.demo.reg_login.dto.article.ArticleUpdateRequest;
import com.spring.demo.reg_login.entity.Article;
import com.spring.demo.reg_login.service.ArticleService;
import com.spring.demo.reg_login.utils.ThreadLocalUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    // GET /article/list → 文章列表
    @GetMapping("/article/list")
    public Result<List<Article>> list() {
        return Result.success(articleService.list());
    }

    // POST /article/add → 新增文章
    @PostMapping("/article/add")
    public Result<String> add(@RequestBody ArticleRequest request) {

        articleService.add(
                request.getTitle(),
                request.getContent(),
                request.getCoverImg(),
                request.getCategoryId()
        );

        return Result.success(
                "发布成功"
        );

    }

    // GET /article/detail → 文章详情
    @GetMapping("/article/detail")
    public Result<Article> detail(@RequestParam Long id) {
        return Result.success(articleService.detail(id));
    }

    // POST /article/update → 修改文章（仅作者本人）
    @PostMapping("/article/update")
    public Result<String> update(@RequestBody ArticleUpdateRequest request) {

        articleService.update(
                request.getId(),
                request.getTitle(),
                request.getContent(),
                request.getCoverImg(),
                request.getCategoryId(),
                ThreadLocalUtil.get()
        );

        return Result.success(
                "修改成功"
        );

    }

    // 删除
    @PostMapping("/article/delete")
    public Result<String> delete(@RequestBody ArticleDeleteRequest request) {

        articleService.delete(request.getId(), ThreadLocalUtil.get());

        return Result.success("删除成功");

    }

    // 分页查询
    @GetMapping("/article/page")
    public Result<PageResult<Article>> page(
        Integer pageNum,
        Integer pageSize, 
        String title,
        String author,
        Long categoryId
    ){

        return Result.success(
            articleService.page(pageNum, pageSize, title, author, categoryId)
        );

    }

    // 查询我的文章
    @GetMapping("/article/my")
    public Result<List<Article>> myList() {

        return Result.success(

            articleService.myList()

        );

    }

    // 分页查询我的文章
    @GetMapping("/article/my/page")
    public Result<PageResult<Article>> myPage(
        Integer pageNum,
        Integer pageSize
    ) {

        return Result.success(
            articleService.myPage(pageNum, pageSize)
        );

    }
}
