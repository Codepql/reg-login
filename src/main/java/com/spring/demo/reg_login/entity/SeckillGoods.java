package com.spring.demo.reg_login.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class SeckillGoods {
    private Long id;

    private String name;

    private Integer stock;

    private LocalDateTime createTime;
}
