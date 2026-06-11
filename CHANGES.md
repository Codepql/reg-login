# 📋 Code Review 改动文档

> 📅 2026/06/11 — 秒杀模块 + ArticleService 代码审查与修复

---

## 一、改动概览

| 文件 | 改动类型 | 说明 |
|------|:---:|------|
| `ArticleService.java` | 🔧 修复 | 替换 Netty 内部 API + 分布式锁升级 |
| `SeckillGoodsService.java` | 🔧 修复 + 🧹 清理 | 旧方案残留、NPE 防护、initStock 完善 |
| `SeckillOrderMapper.java` | 🗑️ 删除 | 移除未使用的 countOrder 方法 |
| `SeckillGoodsMapper.java` | 🔧 修复 | reduceStock 增加 stock > 0 防超卖 |
| `init.sql` | ➕ 新增 | 添加 seckill_goods / seckill_order 建表 DDL |
| `README.md` | 📝 更新 | 秒杀模块文档 + Redis Key + 更新日志 |

---

## 二、逐文件详情

### 1. `ArticleService.java` — 2 个严重问题修复

#### 🔴 问题 A：使用了 Netty 内部 API

```diff
- import io.netty.util.internal.ThreadLocalRandom;
+ import java.util.concurrent.ThreadLocalRandom;
```

- **问题**：`io.netty.util.internal` 包名中的 `internal` 表明这是 Netty 内部实现，不保证向后兼容，随时可能被移除
- **影响**：Netty 升级版本时编译可能直接报错
- **修复**：使用 JDK 标准库 `java.util.concurrent.ThreadLocalRandom`

#### 🔴 问题 B：分布式锁删除不安全（锁被误删）

```diff
- // 旧代码：直接 delete，不检查锁归属
- redisTemplate.delete(lockKey);

+ // 新代码：Lua 脚本原子检查锁值后再删除
+ private static final DefaultRedisScript<Long> UNLOCK_SCRIPT;
+ static {
+     UNLOCK_SCRIPT.setScriptText("""
+         if redis.call('get', KEYS[1]) == ARGV[1]
+         then
+             return redis.call('del', KEYS[1])
+         else
+             return 0
+         end
+     """);
+ }
+ // 使用
+ redisTemplate.execute(UNLOCK_SCRIPT,
+     Collections.singletonList(lockKey), lockValue);
```

- **问题**：旧代码锁 value 固定为 `"1"`，任何线程都能删除其他线程的锁
- **场景**：线程 A 的锁过期 → 线程 B 获取锁 → 线程 A 的 finally 删除 B 的锁 → 线程 C 获取锁 → A、B、C 并发执行，缓存击穿
- **修复**：采用 UUID 锁值 + Lua 原子脚本校验（与 SeckillGoodsService 一致）

#### 🟢 优化：增加双重检查（Double Check）

```java
// 拿到锁后再次检查缓存，避免重复查询 MySQL
json = redisTemplate.opsForValue().get(key);
if (json != null) { ... return; }
```

---

### 2. `SeckillGoodsService.java` — 5 个问题修复

#### 🟡 问题 A：常量定义但未使用（旧方案残留）

```diff
- String userSetKey = "seckill:user:" + goodsId;   // 硬编码
+ String userSetKey = USER_SET_PREFIX + goodsId;    // 使用常量

- String stockKey = "seckill:stock:" + goodsId;     // 硬编码
+ String stockKey = STOCK_PREFIX + goodsId;          // 使用常量
```

- 新增 `STOCK_PREFIX` 常量
- 所有 Redis Key 前缀统一使用常量，便于维护

#### 🟠 问题 B：`initStock` 没有清理 Redis 用户集合

```diff
  redisTemplate.opsForValue().set(stockKey, goods.getStock().toString());
+ // 清空已购买用户记录，否则之前抢到的用户无法再次参与
+ redisTemplate.delete(userSetKey);
```

- **场景**：商品补货后重新秒杀，但上次已购用户的 ID 仍在 Redis Set 中，`sismember` 返回 1，导致他们无法再次购买

#### 🟢 问题 C：缺少空指针防护

```diff
  String username = ThreadLocalUtil.get();
+ if (username == null) {
+     throw new RuntimeException("用户未登录");
+ }

  User user = userMapper.findByUsername(username);
+ if (user == null) {
+     throw new RuntimeException("用户不存在");
+ }
```

#### 🟢 问题 D：goodsId 参数校验

```diff
+ if (goodsId == null) {
+     throw new RuntimeException("商品ID不能为空");
+ }
```

#### 🧹 问题 E：字段声明顺序整理

将常量、final 字段按逻辑分组排列，消除中间插入的零散声明。

---

### 3. `SeckillOrderMapper.java` — 删除未使用方法

```diff
- // 查询是否已经秒杀过了
- @Select("select count(*) from seckill_order where user_id = #{userId} and goods_id = #{goodsId}")
- Long countOrder(Long userId, Long goodsId);
```

- **原因**：秒杀流程已改用 Redis Lua 脚本的 `sismember` 判断是否重复购买，MySQL 查询不再需要
- **这是典型的旧方案残留**：早期可能用 MySQL 查重，后改为 Redis 原子操作，但忘记删除旧代码

---

### 4. `SeckillGoodsMapper.java` — 防超卖

```diff
- @Update("update seckill_goods set stock = stock - 1 where id = #{id}")
- void reduceStock(Long id);

+ @Update("update seckill_goods set stock = stock - 1 where id = #{id} and stock > 0")
+ int reduceStock(Long id);
```

- 增加 `stock > 0` 条件，防止 MySQL 层面库存扣为负数
- 返回值改为 `int`（受影响行数），可用于判断是否扣减成功

---

### 5. `init.sql` — 新增秒杀表 DDL

```sql
-- 6. 秒杀商品表
CREATE TABLE IF NOT EXISTS `seckill_goods` (...)

-- 7. 秒杀订单表
CREATE TABLE IF NOT EXISTS `seckill_order` (...)
```

- 补充数据库初始化脚本，确保新环境可一键建表

---

### 6. `README.md` — 文档同步更新

- 新增 **⚡ 秒杀模块** 功能说明（含流程图）
- 新增秒杀 API 接口文档
- 新增秒杀相关 Redis Key 设计
- 新增 `seckill_goods` / `seckill_order` 表结构
- 项目结构图补充秒杀模块文件
- 更新日志新增阶段八（秒杀模块）、阶段九（缓存优化）

---

## 三、Redis-MySQL 一致性说明

当前方案采用 **Redis 优先 + MySQL 最终一致** 策略：

```
Lua 原子操作（Redis）
  ├── 检查库存 ✓
  ├── 扣减库存 ✓
  └── 记录用户 ✓
       ↓
同步写入 MySQL
  ├── reduceStock
  └── insert order
```

- **正常情况**：Redis 和 MySQL 数据一致
- **MySQL 写入失败**：Redis 已扣减但 MySQL 未记录（**已知权衡**）
  - 秒杀场景优先保证不超卖，允许极少数订单丢失
  - 可通过定时任务对账（Redis Set vs MySQL order）来补偿
- **Redis 宕机**：需从 MySQL 重建缓存（调用 `initStock`）

---

## 四、总结

| 类别 | 数量 | 关键项 |
|------|:---:|------|
| 🔴 严重 | 2 | Netty 内部 API、分布式锁反模式 |
| 🟡 旧残留 | 2 | countOrder 死代码、常量未使用 |
| 🟠 逻辑缺陷 | 2 | initStock 未清 Set、MySQL 无 stock>0 |
| 🟢 防御性 | 3 | NPE 防护、参数校验、双重检查 |
| 📝 文档 | 1 | README 全量更新 |
