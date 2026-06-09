package com.spring.demo.reg_login.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.demo.reg_login.entity.Comment;
import com.spring.demo.reg_login.mapper.CommentMapper;
import com.spring.demo.reg_login.utils.ThreadLocalUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;

    // 发表评论
    public void add(Long articleId, String content) {

        Comment comment = new Comment();

        comment.setArticleId(articleId);

        comment.setContent(content);

        comment.setCreateUser(
            ThreadLocalUtil.get()
        );

        commentMapper.insert(
            comment
        );

    }

    // 查询文章评论
    public List<Comment> list(Long articleId) {

        return commentMapper.findByArticleId(
                articleId
        );

    }

    // 删除评论
    public void delete(Long id) {

        Comment comment = commentMapper.findById(id);

        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }

        if (!comment.getCreateUser().equals(
                ThreadLocalUtil.get()
        )) {
            throw new RuntimeException("只能删除自己的评论");
        }

        commentMapper.deleteById(id);

    }



}
