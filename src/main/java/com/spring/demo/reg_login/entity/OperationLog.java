package com.spring.demo.reg_login.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OperationLog {
    private Long id;

    private String username;

    private String operation;

    private LocalDateTime createTime;
}
