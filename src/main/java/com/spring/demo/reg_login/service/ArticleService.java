package com.spring.demo.reg_login.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.spring.demo.reg_login.common.PageResult;
import com.spring.demo.reg_login.entity.Article;
import com.spring.demo.reg_login.mapper.ArticleMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleMapper articleMapper;

    // list
    public List<Article> list() {

        return articleMapper.findAll();

    }

    // 新增
    public void add(String title, String content, String author) {

        Article article = new Article();

        article.setTitle(title);
        article.setContent(content);
        article.setAuthor(author);

        articleMapper.insert(article);

    }

    // 查详情
    public Article detail(Long id) {

        return articleMapper.findById(id);

    }

    // 修改
    public void update(Long id, String title, String content, String username) {

        Article article = articleMapper.findById(id);

        if (article == null) {
            throw new RuntimeException("博客不存在");
        }

        if (!article.getAuthor().equals(username)) {
            throw new RuntimeException("只能修改自己的博客！");
        }

        article.setTitle(title);
        article.setContent(content);

        articleMapper.update(article);
    }

    // 删除
    public void delete(Long id,String username){

        Article article = articleMapper.findById(id);

        if(article == null){
            throw new RuntimeException("博客不存在");
        }

        if(!article.getAuthor().equals(username)){
            throw new RuntimeException("只能删除自己的博客！");
        }

        articleMapper.deleteById(id);

    }

    // 分页查询
    public PageResult<Article> page(

        Integer pageNum,
        Integer pageSize, 
        String title, 
        String author
    ){

        PageHelper.startPage(pageNum, pageSize);

        Page<Article> page = (Page<Article>)articleMapper.page(title,author);

        PageResult<Article> result = new PageResult<>();

        result.setTotal(page.getTotal());

        result.setList(page.getResult());

        return result;

    }

}
