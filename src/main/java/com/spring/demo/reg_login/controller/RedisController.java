package com.spring.demo.reg_login.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spring.demo.reg_login.common.Result;
import com.spring.demo.reg_login.utils.ThreadLocalUtil;

@RestController
@RequiredArgsConstructor
public class RedisController {

    // 测试
    @GetMapping("/currentUser")
    
    public Result<String> currentUser() {
        return Result.success(
                ThreadLocalUtil.get()
        );

    }
}
