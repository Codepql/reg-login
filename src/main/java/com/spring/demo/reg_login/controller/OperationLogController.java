package com.spring.demo.reg_login.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.demo.reg_login.common.Result;
import com.spring.demo.reg_login.entity.OperationLog;
import com.spring.demo.reg_login.service.OperationLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OperationLogController {
    private final OperationLogService operationLogService;

    @GetMapping("/log/list")
    public Result<List<OperationLog>> list() {

        return Result.success(
                operationLogService.list()
        );
    }
}
