package com.spring.demo.reg_login.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.spring.demo.reg_login.entity.SeckillGoods;

@Mapper
public interface SeckillGoodsMapper {

    // 查询秒杀商品列表
    @Select("""
        select *
        from seckill_goods
        order by id desc
    """)
    List<SeckillGoods> list();

    // 查询商品
    @Select("""
        select *
        from seckill_goods
        where id = #{id}
    """)
    SeckillGoods findById(Long id);

    // 扣减库存（stock > 0 防止超卖）
    @Update("""
        update seckill_goods
        set stock = stock - 1
        where id = #{id}
          and stock > 0
    """)
    int reduceStock(Long id);
}
