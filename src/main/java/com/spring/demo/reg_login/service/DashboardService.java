package com.spring.demo.reg_login.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.spring.demo.reg_login.mapper.ArticleMapper;
import com.spring.demo.reg_login.mapper.CommentMapper;
import com.spring.demo.reg_login.mapper.UserMapper;
import com.spring.demo.reg_login.vo.DashboardVO;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserMapper userMapper;
    private final ArticleMapper articleMapper;
    private final CommentMapper commentMapper;

    public DashboardVO info() {
        DashboardVO vo = new DashboardVO();
        vo.setUserCount(userMapper.count());
        vo.setArticleCount(articleMapper.count());
        vo.setCommentCount(commentMapper.count());
        vo.setLikeCount(articleMapper.totalLikeCount());
        vo.setViewCount(articleMapper.totalViewCount());
        return vo;
    }

}
