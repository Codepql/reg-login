package com.spring.demo.reg_login.common;

import java.util.List;

import lombok.Data;

@Data
public class PageResult<T> {
    private Long total;

    private List<T> list;
}
