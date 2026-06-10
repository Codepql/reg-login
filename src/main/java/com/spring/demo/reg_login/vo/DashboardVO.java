package com.spring.demo.reg_login.vo;

import lombok.Data;

@Data
public class DashboardVO {
    private Long userCount;

    private Long articleCount;

    private Long commentCount;

    private Long likeCount;

    private Long viewCount;
}
