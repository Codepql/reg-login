package com.spring.demo.reg_login.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.spring.demo.reg_login.entity.SeckillOrder;

@Mapper
public interface SeckillOrderMapper {

    // 插入秒杀订单
    @Insert("""
        insert into seckill_order(
            user_id,
            goods_id
        )
        values(
            #{userId},
            #{goodsId}
        )
    """)
    void insert(SeckillOrder order);

    
}
