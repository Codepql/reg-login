package com.spring.demo.reg_login.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.spring.demo.reg_login.common.Result;

// 全局异常处理器：统一拦截所有Controller中抛出的异常，避免把错误堆栈直接返回给前端
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 运行时异常 → 返回具体错误信息给前端，方便用户理解
    @ExceptionHandler(RuntimeException.class)
    public Result<String> runtime(RuntimeException e) {
        e.printStackTrace();
        return Result.error(e.getMessage());
    }

    // 兜底异常 → 其他未预料的Exception，返回统一"系统异常"，不暴露内部细节
    @ExceptionHandler(Exception.class)
    public Result<String> exception(Exception e) {
        e.printStackTrace();
        return Result.error("系统异常");
    }

}
