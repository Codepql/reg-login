package com.spring.demo.reg_login.utils;

public class ThreadLocalUtil {
    private static final ThreadLocal<String> THREAD_LOCAL = new ThreadLocal<>();

    // 保存用户
    public static void set(String username) {
        THREAD_LOCAL.set(username);
    }

    // 获取用户
    public static String get() {
        return THREAD_LOCAL.get();
    }

    // 删除（非常重要）
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
