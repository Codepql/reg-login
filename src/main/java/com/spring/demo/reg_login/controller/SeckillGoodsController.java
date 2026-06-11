package com.spring.demo.reg_login.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.demo.reg_login.common.Result;
import com.spring.demo.reg_login.entity.SeckillGoods;
import com.spring.demo.reg_login.service.SeckillGoodsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SeckillGoodsController {
    
    private final SeckillGoodsService seckillGoodsService;

    // 查询秒杀商品列表
    @GetMapping("/seckill/list")
    public Result<List<SeckillGoods>> list() {

        return Result.success(
            seckillGoodsService.list()
        );
    }

    // 秒杀
    @PostMapping("/seckill")
    public Result<String> seckill(Long goodsId) {

        return Result.success(
                seckillGoodsService.seckill(goodsId)
        );
    }

    // 初始化库存
    @PostMapping("/seckill/init")
    public Result<String> initStock(Long goodsId) {

        return Result.success(
            seckillGoodsService.initStock(goodsId)
        );
    }

    
}
