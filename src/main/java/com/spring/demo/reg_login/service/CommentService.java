package com.spring.demo.reg_login.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.spring.demo.reg_login.entity.Comment;
import com.spring.demo.reg_login.entity.User;
import com.spring.demo.reg_login.mapper.CommentMapper;
import com.spring.demo.reg_login.mapper.UserMapper;
import com.spring.demo.reg_login.utils.ThreadLocalUtil;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    // 发表评论
    public void add(Long articleId, String content) {
        Comment comment = new Comment();
        comment.setArticleId(articleId);
        comment.setContent(content);
        comment.setCreateUser(ThreadLocalUtil.get());
        commentMapper.insert(comment);
    }

    // 查询文章评论
    public List<Comment> list(Long articleId) {
        return commentMapper.findByArticleId(articleId);
    }

    // 删除评论
    public void delete(Long id) {
        Comment comment = commentMapper.findById(id);
        if (comment == null) {
            throw new RuntimeException("评论不存在");
        }
        String username = ThreadLocalUtil.get();
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        boolean isAdmin = "admin".equals(user.getRole());
        boolean isAuthor = username.equals(comment.getCreateUser());
        if (!isAdmin && !isAuthor) {
            throw new RuntimeException("没有权限删除该评论");
        }
        commentMapper.deleteById(id);
    }

}
