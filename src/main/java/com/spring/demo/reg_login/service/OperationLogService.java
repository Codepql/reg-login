package com.spring.demo.reg_login.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.spring.demo.reg_login.entity.OperationLog;
import com.spring.demo.reg_login.mapper.OperationLogMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OperationLogService {
    private final OperationLogMapper operationLogMapper;

    public List<OperationLog> list() {
        return operationLogMapper.list();
    }
}
