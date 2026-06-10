package com.spring.demo.reg_login.utils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// JWT工具类：生成和解析Token（无Spring注解，纯工具类）
public class JwtUtils {

    // 签名密钥（至少256bit=32字符）
    private static final String SECRET = "login-demo-jwt-secret-key-2026-abcdef";

    // HMAC-SHA密钥对象
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    // Token有效期：5天（毫秒值）
    private static final long EXPIRE = 1000L * 60 * 60 * 24 * 5;

    // 生成Token
    public static String generateToken(String username) {
        return Jwts.builder()
                .subject(username)                          // 主题=用户名
                .issuedAt(new Date())                       // 签发时间
                .expiration(new Date(System.currentTimeMillis() + EXPIRE))  // 过期时间
                .signWith(KEY)                              // 用密钥签名
                .compact();                                 // 生成最终字符串
    }

    // 解析Token，返回用户名
    public static String parseToken(String token) {
        return Jwts.parser()
                .verifyWith(KEY)              // 用同一把密钥验证签名
                .build()
                .parseSignedClaims(token)     // 解析Token
                .getPayload()                 // 获取载荷
                .getSubject();                // 获取用户名
    }

}
