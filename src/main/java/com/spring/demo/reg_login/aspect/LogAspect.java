package com.spring.demo.reg_login.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.spring.demo.reg_login.annotation.LogOperation;
import com.spring.demo.reg_login.entity.OperationLog;
import com.spring.demo.reg_login.mapper.OperationLogMapper;
import com.spring.demo.reg_login.utils.ThreadLocalUtil;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {
    private final OperationLogMapper operationLogMapper;

    @AfterReturning("@annotation(logOperation)")
    public void saveLog(
            JoinPoint joinPoint,
            LogOperation logOperation
    ) {

        OperationLog log = new OperationLog();

        log.setUsername(
                ThreadLocalUtil.get()
        );

        log.setOperation(
                logOperation.value()
        );

        operationLogMapper.insert(log);
    }
}
