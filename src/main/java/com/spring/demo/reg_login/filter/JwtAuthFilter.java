package com.spring.demo.reg_login.filter;

import java.io.IOException;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.demo.reg_login.utils.JwtUtils;
import com.spring.demo.reg_login.utils.ThreadLocalUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;  



@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {


    private final StringRedisTemplate redisTemplate;

    // 白名单：无需登录即可访问的接口路径
    private static final String[] WHITE_LIST = {
        "/login",
        "/register",
        "/upload"
    };

    // 判断是否在白名单中
    private boolean isWhiteList(String path){

        for(String item : WHITE_LIST){

            if(path.startsWith(item)){
                return true;
            }

        }

        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // 1️⃣ 放行登录接口
        if (isWhiteList(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2️⃣ 获取 token
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("未登录");
        }

        // 3️⃣ 解析 username（你已有 JwtUtil）
        String username = JwtUtils.parseToken(token);

        // 4️⃣ Redis 校验
        String redisToken = redisTemplate.opsForValue()
                .get("login:" + username);

        if (redisToken == null) {
            throw new RuntimeException("登录已失效");
        }

        if (!redisToken.equals(token)) {
            throw new RuntimeException("登录已失效");
        }

        // 5️⃣ 放行
        try {
            ThreadLocalUtil.set(username);
            filterChain.doFilter(request, response);
        } finally {
            ThreadLocalUtil.remove();
        }
    }
}
