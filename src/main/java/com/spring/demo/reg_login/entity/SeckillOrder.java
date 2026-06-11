package com.spring.demo.reg_login.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SeckillOrder {
    private Long id;

    private Long userId;

    private Long goodsId;

    private LocalDateTime createTime;
}
