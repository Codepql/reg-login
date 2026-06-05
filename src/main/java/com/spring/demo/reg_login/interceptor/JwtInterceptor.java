package com.spring.demo.reg_login.interceptor;

import com.spring.demo.reg_login.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

// 实现HandlerInterceptor接口：Spring拦截器，在请求到达Controller之前/之后执行
public class JwtInterceptor implements HandlerInterceptor {

    // preHandle: 在Controller方法执行前调用，返回true放行、false拦截
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 1. 从请求头中获取Token（Authorization字段）
        String token = request.getHeader("Authorization");

        // 2. Token为空 → 返回401，提示登录
        if (token == null || token.isEmpty()) {
            response.setStatus(401);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("请先登录");
            return false;
        }

        // 3. 解析Token → 解析失败则Token无效
        try {
            String username = JwtUtils.parseToken(token);
            request.setAttribute("username", username);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("Token无效");
            return false;
        }
    }

}
