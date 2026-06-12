# 🚀 Reg-Login — Spring Boot 全栈博客系统

> 📝 一个基于 **Spring Boot 3** + **MyBatis** + **MySQL** + **Redis** + **JWT** 的用户注册登录 & 博客管理系统

---

## 📋 目录

- [🏆 功能概览](#-功能概览)
- [🛠️ 技术栈](#️-技术栈)
- [📁 项目结构](#-项目结构)
- [🗄️ 数据库设计](#️-数据库设计)
- [📡 API 接口文档](#-api-接口文档)
- [🔐 认证架构](#-认证架构)
- [⚡ 快速启动](#-快速启动)
- [📝 更新日志](#-更新日志)

---

## 🏆 功能概览

### 🌱 基础框架
| 模块 | 状态 | 说明 |
|------|:----:|------|
| ✅ Spring Boot 3 | 🟢 | 主框架，版本 `3.5.15`，Java 17 |
| ✅ MyBatis | 🟢 | 注解式 SQL，零 XML 配置 |
| ✅ MySQL | 🟢 | 关系型数据库，utf8mb4 编码 |
| ✅ Lombok | 🟢 | 消除样板代码（`@Data`、`@RequiredArgsConstructor`） |
| ✅ Redis | 🟢 | 缓存 Token 与点赞数据 |
| ✅ PageHelper | 🟢 | 物理分页，自动拼接 `LIMIT` |

### 👤 用户模块
```
📝 注册 ──→ 🔍 用户名重复校验 ──→ 🔐 BCrypt 密码加密 ──→ 💾 存入 MySQL
🔑 登录 ──→ 🔍 密码校验 ──→ 🎫 JWT 生成 ──→ 🟥 Redis 存储 ──→ 📤 返回 Token
🚪 登出 ──→ 🗑️ Redis 删除 Token ──→ ❌ Token 立即失效
```

| 功能 | 状态 | 说明 |
|------|:----:|------|
| ✅ 注册 | 🟢 | 用户名唯一性校验 + BCrypt 加密 |
| ✅ 登录 | 🟢 | 密码匹配 → JWT 签发 → Redis 存储 |
| ✅ 登出 | 🟢 | 删除 Redis 中 Token，立即失效 |
| ✅ 获取当前用户 | 🟢 | ThreadLocal + `/me` + `/currentUser` |
| ✅ 用户列表 | 🟢 | 管理员可查看所有用户（VO，不暴露密码） |
| ✅ 角色管理 | 🟢 | 管理员可修改用户角色（admin/user） |
| ✅ 状态管理 | 🟢 | 管理员可封禁/解封用户 |
| ✅ 分页查询用户 | 🟢 | 支持按用户名模糊搜索 |

### 🔐 登录认证体系
```
🌐 HTTP 请求
    ↓
🛡️ JwtAuthFilter（OncePerRequestFilter，每次请求只执行一次）
    ↓
🏷️  白名单检查 ──→ /login、/register、/upload 直接放行
    ↓
🔍 解析 Token（Header: Authorization）
    ↓
🟥 Redis 校验（Token 存在 & 未过期 & 值一致）
    ↓
🧵 ThreadLocal 保存当前用户名
    ↓
🎮 Controller 处理业务
    ↓
⚙️ Service 层
    ↓
🗄️ Mapper → MySQL
    ↓
🧹 finally: ThreadLocal.remove()（防止内存泄漏）
```

| 功能 | 状态 | 说明 |
|------|:----:|------|
| ✅ JWT 生成 | 🟢 | HMAC-SHA256 签名，5 天有效期 |
| ✅ JWT 解析 | 🟢 | 签名验证 → 提取用户名 |
| ✅ Filter 拦截 | 🟢 | 全局统一认证，白名单放行 |
| ✅ Redis 存储 Token | 🟢 | Key: `login:{username}`，7 天过期 |
| ✅ ThreadLocal | 🟢 | 线程隔离存储当前用户，`finally` 确保清理 |
| ✅ 全局异常处理 | 🟢 | `@RestControllerAdvice` 统一捕获异常 |

### 📝 博客模块
```
✍️  发布 ──→ 📤 上传封面图 ──→ 💾 存入 MySQL
📖 详情 ──→ 🔍 按 ID 查询 ──→ 🟥 Redis 缓存 ──→ 📄 返回完整信息
✏️  修改 ──→ 🔒 作者校验 ──→ 🖼️ 旧图清理 ──→ 🗑️ 缓存清除 ──→ 💾 更新
🗑️ 删除 ──→ 🔒 作者/管理员校验 ──→ 🖼️ 封面图清理 ──→ 🗑️ 缓存清除 ──→ ❌ 删除记录
```

| 功能 | 状态 | 说明 |
|------|:----:|------|
| ✅ 发布文章 | 🟢 | 标题 + 内容 + 封面图 URL + 分类 |
| ✅ 文章列表 | 🟢 | 按 ID 倒序 |
| ✅ 文章详情 | 🟢 | Redis 缓存 + 浏览量自动增加 |
| ✅ 修改文章 | 🟢 | 仅作者本人可修改 |
| ✅ 删除文章 | 🟢 | 作者本人或管理员可删除 |
| ✅ 分页查询 | 🟢 | PageHelper 物理分页，支持标题/作者/分类筛选 |
| ✅ 我的文章 | 🟢 | 查询当前用户的文章（列表 + 分页） |
| ✅ 热门文章 | 🟢 | 按点赞数倒序 TOP 10 |

### 📂 分类模块
| 功能 | 状态 | 说明 |
|------|:----:|------|
| ✅ 分类列表 | 🟢 | 查询所有分类 |
| ✅ 新增分类 | 🟢 | 记录创建人 |
| ✅ 修改分类 | 🟢 | 按 ID 更新分类名 |
| ✅ 删除分类 | 🟢 | 按 ID 删除 |

### 💬 评论模块
| 功能 | 状态 | 说明 |
|------|:----:|------|
| ✅ 发表评论 | 🟢 | 文章 ID + 内容，记录评论人 |
| ✅ 评论列表 | 🟢 | 按文章 ID 查询，ID 倒序 |
| ✅ 删除评论 | 🟢 | 评论人本人或管理员可删除 |

### ❤️ 点赞模块（Redis Set）
```
👍 点赞 ──→ 🟥 SADD article:{id}:like {username} ──→ 📊 like_count +1
👎 取消  ──→ 🟥 SREM article:{id}:like {username} ──→ 📊 like_count -1
🔢 数量  ──→ 🟥 SCARD article:{id}:like
❓ 状态  ──→ 🟥 SISMEMBER article:{id}:like {username}
```

| 功能 | 状态 | 说明 |
|------|:----:|------|
| ✅ 点赞 | 🟢 | Redis Set 存储，防重复点赞 |
| ✅ 取消点赞 | 🟢 | 从 Set 中移除 |
| ✅ 点赞数量 | 🟢 | SCARD 直接获取 |
| ✅ 点赞状态 | 🟢 | SISMEMBER 判断是否已点赞 |

### ⚡ 秒杀模块（Redis 分布式锁 + Lua 原子操作）
```
⚡ 秒杀请求
    ↓
🔒 获取分布式锁（Redis setIfAbsent + UUID）
    ↓
🧵 获取当前用户（ThreadLocal）
    ↓
📜 Lua 原子脚本执行：
  ├── 🔍 SISMEMBER 检查是否已购买
  ├── 📊 GET 检查库存是否充足
  ├── 📉 DECR 扣减 Redis 库存
  └── 👤 SADD 记录已购买用户
    ↓
💾 异步同步 MySQL（扣库存 + 插订单）
    ↓
🔓 Lua 脚本安全释放锁
```

| 功能 | 状态 | 说明 |
|------|:----:|------|
| ✅ 商品列表 | 🟢 | 查询所有秒杀商品 |
| ✅ 秒杀抢购 | 🟢 | Redis Lua 原子操作 + 分布式锁 |
| ✅ 初始化库存 | 🟢 | MySQL → Redis 同步 + 清空用户记录 |
| ✅ 防重复购买 | 🟢 | Redis Set 记录已购买用户 |
| ✅ 缓存空值防穿透 | 🟢 | 文章缓存空值防恶意查询 |

### 📊 仪表盘模块
| 功能 | 状态 | 说明 |
|------|:----:|------|
| ✅ 统计数据 | 🟢 | 用户数、文章数、评论数、点赞总数、浏览总数 |

### 📁 文件模块
```
📤 上传图片
    ↓
🏷️  UUID 重命名（防冲突）
    ↓
📁 本地存储（D:/Full_Stack/project/spring-boot/upload/）
    ↓
🔗 返回访问 URL（http://localhost:8080/upload/xxx.jpg）
    ↓
🖼️  博客封面引用
    ↓
🗑️ 删除文章时自动清理旧图片
```

| 功能 | 状态 | 说明 |
|------|:----:|------|
| ✅ 图片上传 | 🟢 | MultipartFile + UUID 重命名 |
| ✅ 静态资源映射 | 🟢 | `/upload/**` → 本地目录 |
| ✅ 封面保存 | 🟢 | 博客新增/修改时存储 `coverImg` |
| ✅ 旧图清理 | 🟢 | 修改/删除文章时自动删除旧封面 |

---

## 🛠️ 技术栈

| 层级 | 技术 | 版本 | 图标 |
|------|------|------|:----:|
| ☕ 语言 | Java | 17 | ☕ |
| 🏗️ 框架 | Spring Boot | 3.5.15 | 🍃 |
| 🗺️ ORM | MyBatis | 3.0.5 | 🗺️ |
| 🗄️ 数据库 | MySQL | 8.0+ | 🐬 |
| 🟥 缓存 | Redis | 7.0+ | 🟥 |
| 🔐 认证 | JJWT (io.jsonwebtoken) | 0.12.7 | 🎫 |
| 🔒 加密 | Spring Security Crypto (BCrypt) | — | 🔒 |
| 📄 分页 | PageHelper | 2.1.1 | 📖 |
| 📦 JSON | Fastjson2 | 2.0.57 | 📦 |
| 🧹 简化 | Lombok | — | 🧹 |
| 🔥 热重载 | DevTools | — | 🔥 |
| 🧪 测试 | JUnit 5 + Mockito | — | 🧪 |

---

## 📁 项目结构

```
reg-login/
├── 📄 pom.xml                              # Maven 依赖配置
├── 📄 README.md                            # 本文件 📖
│
├── 📂 src/main/java/com/spring/demo/reg_login/
│   ├── 🚀 RegLoginApplication.java         # Spring Boot 启动类
│   │
│   ├── 📂 config/
│   │   └── ⚙️ WebConfig.java               # CORS + 静态资源映射
│   │
│   ├── 📂 common/
│   │   ├── 📦 Result.java                  # 统一响应体 {code, message, data}
│   │   └── 📦 PageResult.java              # 分页响应体 {total, list}
│   │
│   ├── 📂 controller/
│   │   ├── 👋 HelloController.java         # 测试接口
│   │   ├── 👤 UserController.java          # 用户接口（注册/登录/登出/用户管理）
│   │   ├── 📝 ArticleController.java       # 文章接口（CRUD + 分页 + 热门）
│   │   ├── 📂 CategoryController.java      # 分类接口（CRUD）
│   │   ├── 💬 CommentController.java       # 评论接口（发表/查询/删除）
│   │   ├── ❤️ LikeController.java          # 点赞接口（点赞/取消/数量/状态）
│   │   ├── 📤 UploadController.java        # 文件上传
│   │   ├── 🟥 RedisController.java         # 当前用户测试
│   │   ├── 📊 DashboardController.java     # 仪表盘统计
│   │   └── ⚡ SeckillGoodsController.java   # 秒杀接口
│   │
│   ├── 📂 service/
│   │   ├── 👤 UserService.java             # 注册/登录/用户管理
│   │   ├── 📝 ArticleService.java          # 文章 CRUD + 缓存 + 权限
│   │   ├── 📂 CategoryService.java         # 分类 CRUD
│   │   ├── 💬 CommentService.java          # 评论 发表/查询/删除
│   │   ├── ❤️ LikeService.java             # 点赞逻辑（Redis Set）
│   │   ├── 📊 DashboardService.java        # 仪表盘统计
│   │   └── ⚡ SeckillGoodsService.java      # 秒杀逻辑（分布式锁 + Lua）
│   │
│   ├── 📂 mapper/
│   │   ├── 👤 UserMapper.java              # 用户 SQL（注解式）
│   │   ├── 📝 ArticleMapper.java           # 文章 SQL（注解式 + 动态SQL）
│   │   ├── 📂 CategoryMapper.java          # 分类 SQL
│   │   ├── 💬 CommentMapper.java           # 评论 SQL
│   │   ├── ⚡ SeckillGoodsMapper.java       # 秒杀商品 SQL
│   │   └── ⚡ SeckillOrderMapper.java       # 秒杀订单 SQL
│   │
│   ├── 📂 entity/
│   │   ├── 👤 User.java                    # 用户实体
│   │   ├── 📝 Article.java                 # 文章实体
│   │   ├── 📂 Category.java                # 分类实体
│   │   ├── 💬 Comment.java                 # 评论实体
│   │   ├── ⚡ SeckillGoods.java             # 秒杀商品实体
│   │   └── ⚡ SeckillOrder.java             # 秒杀订单实体
│   │
│   ├── 📂 dto/
│   │   ├── 📥 LoginRequest.java            # 登录请求 DTO
│   │   ├── 📥 RegisterRequest.java         # 注册请求 DTO
│   │   ├── 📂 article/
│   │   │   ├── 📥 ArticleRequest.java          # 新增文章 DTO
│   │   │   ├── 📥 ArticleUpdateRequest.java    # 修改文章 DTO
│   │   │   └── 📥 ArticleDeleteRequest.java    # 删除文章 DTO
│   │   ├── 📂 category/
│   │   │   ├── 📥 CategoryRequest.java         # 新增分类 DTO
│   │   │   └── 📥 CategoryUpdateRequest.java   # 修改分类 DTO
│   │   ├── 📂 comment/
│   │   │   └── 📥 CommentAddRequest.java       # 新增评论 DTO
│   │   └── 📂 user/
│   │       ├── 📥 UserRoleDTO.java             # 更新角色 DTO
│   │       └── 📥 UserStatusDTO.java           # 更新状态 DTO
│   │
│   ├── 📂 vo/
│   │   ├── 👤 UserVO.java                  # 用户视图对象（不暴露密码）
│   │   └── 📊 DashboardVO.java             # 仪表盘视图对象
│   │
│   ├── 📂 filter/
│   │   └── 🛡️ JwtAuthFilter.java           # JWT 认证过滤器（核心🔐）
│   │
│   ├── 📂 utils/
│   │   ├── 🎫 JwtUtils.java                # JWT 生成 & 解析
│   │   ├── 📁 FileUtil.java                # 文件删除工具
│   │   └── 🧵 ThreadLocalUtil.java         # 线程级用户存储
│   │
│   └── 📂 exception/
│       └── 🚨 GlobalExceptionHandler.java  # 全局异常处理
│
├── 📂 src/main/resources/
│   ├── ⚙️ application.properties           # 应用配置
│   └── 📂 sql/
│       └── 🗄️ init.sql                     # 数据库初始化脚本
│
└── 📂 src/test/java/.../
    └── 🧪 RegLoginApplicationTests.java    # 测试类
```

---

## 🗄️ 数据库设计

### 📊 user 表
| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| 🔑 `id` | `BIGINT` | PK, AUTO_INCREMENT | 主键 |
| 👤 `username` | `VARCHAR(50)` | NOT NULL, UNIQUE | 用户名 |
| 🔒 `password` | `VARCHAR(100)` | NOT NULL | BCrypt 密文 |
| 🎭 `role` | `VARCHAR(20)` | NOT NULL, DEFAULT 'user' | 角色（user / admin） |
| 🔘 `status` | `INT` | NOT NULL, DEFAULT 1 | 状态（1: 正常, 0: 封禁） |
| 🕐 `create_time` | `DATETIME` | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 📊 category 表
| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| 🔑 `id` | `BIGINT` | PK, AUTO_INCREMENT | 主键 |
| 📂 `category_name` | `VARCHAR(50)` | NOT NULL, UNIQUE | 分类名称 |
| 👤 `create_user` | `VARCHAR(50)` | NOT NULL | 创建人 |
| 🕐 `create_time` | `DATETIME` | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 📊 article 表
| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| 🔑 `id` | `BIGINT` | PK, AUTO_INCREMENT | 主键 |
| 📌 `title` | `VARCHAR(200)` | NOT NULL | 文章标题 |
| 📝 `content` | `TEXT` | NOT NULL | 文章内容 |
| 👤 `author` | `VARCHAR(50)` | NOT NULL | 作者（用户名） |
| 🖼️ `cover_img` | `VARCHAR(500)` | — | 封面图片 URL |
| 📂 `category_id` | `BIGINT` | — | 分类 ID |
| ❤️ `like_count` | `BIGINT` | NOT NULL, DEFAULT 0 | 点赞数 |
| 👁️ `view_count` | `BIGINT` | NOT NULL, DEFAULT 0 | 浏览量 |
| 🕐 `create_time` | `DATETIME` | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 📊 comment 表
| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| 🔑 `id` | `BIGINT` | PK, AUTO_INCREMENT | 主键 |
| 📝 `article_id` | `BIGINT` | NOT NULL | 文章 ID |
| 💬 `content` | `TEXT` | NOT NULL | 评论内容 |
| 👤 `create_user` | `VARCHAR(50)` | NOT NULL | 评论人 |
| 🕐 `create_time` | `DATETIME` | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 📊 seckill_goods 表
| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| 🔑 `id` | `BIGINT` | PK, AUTO_INCREMENT | 主键 |
| 📛 `name` | `VARCHAR(200)` | NOT NULL | 商品名称 |
| 📦 `stock` | `INT` | NOT NULL, DEFAULT 0 | 库存数量 |
| 🕐 `create_time` | `DATETIME` | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 📊 seckill_order 表
| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| 🔑 `id` | `BIGINT` | PK, AUTO_INCREMENT | 主键 |
| 👤 `user_id` | `BIGINT` | NOT NULL | 用户 ID |
| 🎁 `goods_id` | `BIGINT` | NOT NULL | 商品 ID |
| 🕐 `create_time` | `DATETIME` | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 🟥 Redis Key 设计
| Key | 类型 | Value | TTL | 说明 |
|------|------|------|-----|------|
| `login:{username}` | String | JWT Token | 7 天 | 登录状态，登出时删除 |
| `article:detail:{id}` | String | Article JSON | 永久 | 文章详情缓存 |
| `article:{id}:like` | Set | 用户名集合 | 永久 | 点赞用户集合 |
| `article:lock:{id}` | String | UUID | 10s | 文章缓存击穿锁 |
| `seckill:stock:{id}` | String | 库存数量 | 永久 | 秒杀商品 Redis 库存 |
| `seckill:user:{id}` | Set | 用户ID集合 | 永久 | 已购买该商品的用户 |
| `seckill:lock:{id}` | String | UUID | 10s | 秒杀分布式锁 |

---
## 📡 API 接口文档

> 🌐 基础路径：`http://localhost:8080`
>
> 🔐 除白名单外，所有接口需在 Header 中携带：`Authorization: <token>`

### 🟢 白名单接口（无需登录）

| 方法 | 路径 | 说明 | 请求体 |
|:----:|------|------|--------|
| 🔵 GET | `/hello` | 🧪 测试 | — |
| 🟢 POST | `/register` | 📝 用户注册 | `{username, password}` |
| 🟢 POST | `/login` | 🔑 用户登录 | `{username, password}` |
| 🟢 POST | `/upload` | 📤 图片上传 | `multipart/form-data (file)` |

### 👤 用户接口（需登录）

| 方法 | 路径 | 说明 | 请求体/参数 |
|:----:|------|------|-------------|
| 🔵 GET | `/info` | 🔐 登录用户可访问 | — |
| 🔵 GET | `/currentUser` | 👤 获取当前登录用户 | — |
| 🟠 POST | `/logout` | 🚪 退出登录 | — |
| 🔵 GET | `/user/list` | 📋 用户列表（管理员） | — |
| 🟠 PUT | `/user/role` | 🎭 修改角色（管理员） | `{id, role}` |
| 🟠 PUT | `/user/status` | 🔘 修改状态（管理员） | `{id, status}` |
| 🔵 GET | `/user/page` | 📄 分页查询用户（管理员） | `?pageNum=&pageSize=&username=` |

### 📝 文章接口（需登录）

| 方法 | 路径 | 说明 | 请求体/参数 |
|:----:|------|------|-------------|
| 🔵 GET | `/article/list` | 📋 文章列表 | — |
| 🔵 GET | `/article/detail` | 📖 文章详情 | `?id=1` |
| 🟢 POST | `/article/add` | ✍️ 发布文章 | `{title, content, coverImg, categoryId}` |
| 🟠 POST | `/article/update` | ✏️ 修改文章 | `{id, title, content, coverImg, categoryId}` |
| 🔴 POST | `/article/delete` | 🗑️ 删除文章 | `{id}` |
| 🔵 GET | `/article/page` | 📄 分页搜索 | `?pageNum=&pageSize=&title=&author=&categoryId=` |
| 🔵 GET | `/article/my` | 👤 我的文章 | — |
| 🔵 GET | `/article/my/page` | 📄 我的文章分页 | `?pageNum=&pageSize=` |
| 🔵 GET | `/article/hot` | 🔥 热门文章 TOP10 | — |

### 📂 分类接口（需登录）

| 方法 | 路径 | 说明 | 请求体/参数 |
|:----:|------|------|-------------|
| 🔵 GET | `/category/list` | 📋 分类列表 | — |
| 🟢 POST | `/category/add` | ➕ 新增分类 | `{categoryName}` |
| 🟠 POST | `/category/update` | ✏️ 修改分类 | `{id, categoryName}` |
| 🔴 POST | `/category/delete` | 🗑️ 删除分类 | `?id=1` |

### 💬 评论接口（需登录）

| 方法 | 路径 | 说明 | 请求体/参数 |
|:----:|------|------|-------------|
| 🟢 POST | `/comment/add` | 💬 发表评论 | `{articleId, content}` |
| 🔵 GET | `/comment/list` | 📋 文章评论 | `?articleId=1` |
| 🔴 DELETE | `/comment/delete` | 🗑️ 删除评论 | `?id=1` |

### ❤️ 点赞接口（需登录）

| 方法 | 路径 | 说明 | 参数 |
|:----:|------|------|------|
| 🟢 POST | `/like` | 👍 点赞 | `?articleId=1` |
| 🔴 DELETE | `/like` | 👎 取消点赞 | `?articleId=1` |
| 🔵 GET | `/like/count` | 🔢 点赞数量 | `?articleId=1` |
| 🔵 GET | `/like/status` | ❓ 是否已点赞 | `?articleId=1` |

### ⚡ 秒杀接口（需登录）

| 方法 | 路径 | 说明 | 参数 |
|:----:|------|------|------|
| 🔵 GET | `/seckill/list` | 📋 秒杀商品列表 | — |
| 🟢 POST | `/seckill` | ⚡ 秒杀抢购 | `?goodsId=1` |
| 🟢 POST | `/seckill/init` | 🔄 初始化库存 | `?goodsId=1` |

### 📊 仪表盘接口（需登录）

| 方法 | 路径 | 说明 | 参数 |
|:----:|------|------|------|
| 🔵 GET | `/dashboard` | 📊 系统统计 | — |

### 📦 统一响应格式

```json
// ✅ 成功
{
  "code": 200,
  "message": "success",
  "data": "..."
}

// ❌ 失败
{
  "code": 500,
  "message": "用户名已存在~",
  "data": null
}
```

---

## 🔐 认证架构

### 🔄 完整请求生命周期

```
┌─────────────────────────────────────────────────────────┐
│                     🌐 HTTP Request                      │
└─────────────────────┬───────────────────────────────────┘
                      ▼
         ┌────────────────────────┐
         │  🛡️ JwtAuthFilter      │  ← OncePerRequestFilter
         │  ① 白名单检查           │     /login、/register、/upload 放行
         │  ② 提取 Authorization   │
         │  ③ JWT 解析 → username  │
         │  ④ Redis 校验 Token     │
         │  ⑤ ThreadLocal.set()   │
         └───────────┬────────────┘
                     ▼
         ┌────────────────────────┐
         │  🎮 Controller          │  ← 业务入口
         │  从 ThreadLocal 取用户   │
         └───────────┬────────────┘
                     ▼
         ┌────────────────────────┐
         │  ⚙️ Service             │  ← 业务逻辑 + 权限校验
         └───────────┬────────────┘
                     ▼
         ┌────────────────────────┐
         │  🗄️ Mapper → MySQL     │  ← 数据持久化
         └───────────┬────────────┘
                     ▼
         ┌────────────────────────┐
         │  🧹 ThreadLocal.remove()│  ← finally 块，防止内存泄漏
         └───────────┬────────────┘
                     ▼
         ┌────────────────────────┐
         │  📦 Result<T> JSON     │  ← 统一响应
         └────────────────────────┘
```

### 🔑 JWT 工作流程

```
登录成功 ──→ JwtUtils.generateToken(username)
                │
                ├── subject: username
                ├── issuedAt: 当前时间
                ├── expiration: +5 天
                └── signWith: HMAC-SHA256 密钥
                │
                ▼
            🎫 Token 字符串
                │
                ├──→ 📤 返回给前端（Response Body）
                └──→ 🟥 存入 Redis（Key: login:{username}, TTL: 7天）

后续请求 ──→ Header: Authorization: <token>
                │
                ├──→ 🔍 parseToken(token) → username
                ├──→ 🟥 redis.get("login:" + username)
                └──→ ✅ 一致 → 放行 / ❌ 不一致 → 401
```

### 🚪 登出流程

```
POST /logout
    │
    ├── 🧵 ThreadLocalUtil.get() → 获取当前用户
    ├── 🟥 redisTemplate.delete("login:" + username)
    └── ✅ Token 立即失效（下次请求时 Redis 中找不到）
```

---

## ⚡ 快速启动

### 📋 环境要求

| 工具 | 版本 | 说明 |
|------|------|------|
| ☕ JDK | 17+ | 编译运行 |
| 🐬 MySQL | 8.0+ | 数据库 |
| 🟥 Redis | 7.0+ | Token 缓存 + 点赞数据 |
| 🔧 Maven | 3.8+ | 构建工具 |

### 🚀 启动步骤

```bash
# ① 启动 MySQL & Redis（确保服务运行中）

# ② 初始化数据库（第一次运行）
mysql -u root -p < src/main/resources/sql/init.sql

# ③ 修改配置（按需）
# 编辑 src/main/resources/application.properties
#   - spring.datasource.username / password
#   - spring.data.redis.host / port
#   - file.upload-path（上传目录）

# ④ 启动项目
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run

# ⑤ 验证
curl http://localhost:8080/hello
# → hello spring boot
```

### 🖥️ 前端联调

项目已配置 CORS，允许 `http://localhost:5173`（Vite 默认端口）跨域访问。如需修改：

```java
// WebConfig.java
registry.addMapping("/**")
        .allowedOrigins("http://localhost:5173")  // ← 改为你的前端地址
```

---

## 📝 更新日志

### ✅ 阶段一：基础框架搭建
- 🍃 Spring Boot 3 项目初始化
- 🗺️ MyBatis 集成 + 注解式 SQL
- 🐬 MySQL 数据库连接 + 驼峰命名自动转换
- 🧹 Lombok 集成
- 🔥 DevTools 热重载

### ✅ 阶段二：用户模块
- 📝 用户注册 + 用户名唯一性校验
- 🔒 BCrypt 密码加密存储
- 🔑 用户登录 + 密码校验
- 🎫 JWT Token 生成（JJWT 0.12.7）

### ✅ 阶段三：认证体系升级
- 🟥 Redis 集成，存储登录 Token
- 🛡️ JwtAuthFilter 全局过滤器
- 🧵 ThreadLocal 线程级用户传递
- 🚪 Logout 删除 Redis Token，即时失效
- 🚨 全局异常处理器

### ✅ 阶段四：博客 CRUD
- ✍️ 发布文章（标题 + 内容 + 封面）
- 📋 文章列表
- 📖 文章详情
- ✏️ 修改文章（仅作者本人）
- 🗑️ 删除文章（仅作者本人）

### ✅ 阶段五：高级功能
- 📄 PageHelper 分页查询
- 🔍 标题模糊搜索 + 作者筛选
- 📤 图片上传（UUID 重命名）
- 🖼️ 博客封面图保存
- 🗑️ 修改/删除时自动清理旧图片

### ✅ 阶段六：分类、评论与点赞
- 📂 文章分类管理（CRUD）
- 💬 文章评论（发表/查询/删除）
- ❤️ 点赞/取消点赞（Redis Set 存储）
- 👁️ 浏览量统计
- 🟥 文章详情 Redis 缓存

### ✅ 阶段七：管理员与仪表盘
- 👑 管理员角色（admin/user）
- 🔘 用户状态管理（封禁/解封）
- 📊 仪表盘数据统计
- 📄 用户分页查询
- 👤 用户视图对象（VO，不暴露密码）

### ✅ 阶段八：秒杀模块
- ⚡ 秒杀商品列表查询
- 🔒 Redis 分布式锁（UUID + Lua 安全释放）
- 📜 Lua 原子脚本（库存检查 + 扣减 + 防重复）
- 🟥 Redis Set 记录已购用户
- 💾 Redis → MySQL 最终一致性同步
- 🔄 库存初始化（清空历史购买记录）

### ✅ 阶段九：缓存优化
- 🛡️ 文章缓存空值防穿透（NULL_VALUE）
- 🔒 文章缓存热点锁防击穿（双重检查 + 锁竞争递归）
- 🎲 缓存 TTL 随机化防雪崩
- 🔧 分布式锁升级（UUID 值 + Lua 脚本安全释放）
- 🗑️ 清理旧方案残留（SeckillOrderMapper.countOrder）
- 🐛 修复 Netty 内部 API 引用（ThreadLocalRandom）

---

<p align="center">
  🎯 <b>项目完成度：~98%</b> &nbsp;|&nbsp;
  🐙 <b>适合校招简历</b> &nbsp;|&nbsp;
  🚀 <b>持续迭代中...</b>
</p>

<p align="center">
  <sub>Made with ❤️ by Spring Boot enthusiasts</sub>
</p>
