package com.spring.demo.reg_login.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import com.spring.demo.reg_login.entity.SeckillGoods;
import com.spring.demo.reg_login.entity.SeckillOrder;
import com.spring.demo.reg_login.entity.User;
import com.spring.demo.reg_login.mapper.SeckillGoodsMapper;
import com.spring.demo.reg_login.mapper.SeckillOrderMapper;
import com.spring.demo.reg_login.mapper.UserMapper;
import com.spring.demo.reg_login.utils.ThreadLocalUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeckillGoodsService {

    private final SeckillGoodsMapper seckillGoodsMapper;

    private final SeckillOrderMapper seckillOrderMapper;

    private final UserMapper userMapper;

    private final StringRedisTemplate redisTemplate;

    private static final String LOCK_PREFIX   = "seckill:lock:";
    private static final String STOCK_PREFIX  = "seckill:stock:";
    private static final String USER_SET_PREFIX = "seckill:user:";

    private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;
    private static final DefaultRedisScript<Long> SECKILL_SCRIPT;
    static {

        // Lua脚本：先判断锁的值是否匹配，如果匹配则删除锁，否则不做任何操作
        UNLOCK_SCRIPT = new DefaultRedisScript<>();

        UNLOCK_SCRIPT.setScriptText(
                """
                if redis.call('get', KEYS[1]) == ARGV[1]
                then
                    return redis.call('del', KEYS[1])
                else
                    return 0
                end
                """
        );

        UNLOCK_SCRIPT.setResultType(Long.class);

        SECKILL_SCRIPT = new DefaultRedisScript<>();

        // 返回值：0-成功，1-库存不足，2-已经抢购过了
        SECKILL_SCRIPT.setScriptText(
                """
                if redis.call('sismember', KEYS[2], ARGV[1]) == 1 then
                    return 2
                end

                if tonumber(redis.call('get', KEYS[1])) <= 0 then
                    return 1
                end

                redis.call('decr', KEYS[1])

                redis.call('sadd', KEYS[2], ARGV[1])

                return 0
                """
        );

        SECKILL_SCRIPT.setResultType(Long.class);
    }

    // 查询秒杀商品列表
    public List<SeckillGoods> list() {
        return seckillGoodsMapper.list();
    }

    // 秒杀
    public String seckill(Long goodsId) {

        if (goodsId == null) {
            throw new RuntimeException("商品ID不能为空");
        }

        String lockKey   = LOCK_PREFIX + goodsId;
        String lockValue = UUID.randomUUID().toString();

        Boolean success = redisTemplate.opsForValue()
                        .setIfAbsent(
                                lockKey,
                                lockValue,
                                10,
                                TimeUnit.SECONDS
                        );

        if (Boolean.FALSE.equals(success)) {
            throw new RuntimeException("当前人数过多，请稍后重试");
        }

        try {

            String username = ThreadLocalUtil.get();

            if (username == null) {
                throw new RuntimeException("用户未登录");
            }

            User user = userMapper.findByUsername(username);

            if (user == null) {
                throw new RuntimeException("用户不存在");
            }

            String stockKey   = STOCK_PREFIX + goodsId;
            String userSetKey = USER_SET_PREFIX + goodsId;

            Long result = redisTemplate.execute(
                SECKILL_SCRIPT,
                List.of(stockKey, userSetKey),
                user.getId().toString()
            );

            if (result == null) {
                throw new RuntimeException("系统异常");
            }

            if (result == 1) {
                throw new RuntimeException("库存不足");
            }

            if (result == 2) {
                throw new RuntimeException("您已经抢购过该商品");
            }

            // Lua 脚本成功后，同步写入 MySQL（最终一致性）
            seckillGoodsMapper.reduceStock(goodsId);

            SeckillOrder order = new SeckillOrder();

            order.setUserId(user.getId());

            order.setGoodsId(goodsId);

            seckillOrderMapper.insert(order);

            return "秒杀成功";

        } finally {

            redisTemplate.execute(
            UNLOCK_SCRIPT,
            Collections.singletonList(lockKey),
            lockValue
            );

        }
    }


    // 初始化库存（将 MySQL 库存同步到 Redis，并清空已购买用户记录）
    public String initStock(Long goodsId) {

        if (goodsId == null) {
            throw new RuntimeException("商品ID不能为空");
        }

        SeckillGoods goods = seckillGoodsMapper.findById(goodsId);

        if (goods == null) {
            throw new RuntimeException("商品不存在");
        }

        String stockKey   = STOCK_PREFIX + goodsId;
        String userSetKey = USER_SET_PREFIX + goodsId;

        redisTemplate.opsForValue().set(
                stockKey,
                goods.getStock().toString()
        );

        // 清空已购买用户记录，否则之前抢到的用户无法再次参与
        redisTemplate.delete(userSetKey);

        return "库存同步成功";
    }


}
