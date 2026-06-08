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
| ✅ Redis | 🟢 | 缓存 Token，实现登出即失效 |
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
📖 详情 ──→ 🔍 按 ID 查询 ──→ 📄 返回完整信息
✏️  修改 ──→ 🔒 作者校验 ──→ 🖼️ 旧图清理 ──→ 💾 更新
🗑️ 删除 ──→ 🔒 作者校验 ──→ 🖼️ 封面图清理 ──→ ❌ 删除记录
```

| 功能 | 状态 | 说明 |
|------|:----:|------|
| ✅ 发布文章 | 🟢 | 标题 + 内容 + 封面图 URL |
| ✅ 文章列表 | 🟢 | 按 ID 倒序 |
| ✅ 文章详情 | 🟢 | 按 ID 查询 |
| ✅ 修改文章 | 🟢 | 仅作者本人可修改 |
| ✅ 删除文章 | 🟢 | 仅作者本人可删除 |
| ✅ 分页查询 | 🟢 | PageHelper 物理分页 |
| ✅ 标题模糊搜索 | 🟢 | `LIKE %keyword%` |
| ✅ 作者查询 | 🟢 | 按作者筛选 |

### 📁 文件模块
```
📤 上传图片
    ↓
🏷️  UUID 重命名（防冲突）
    ↓
📁 本地存储（D:/Full_Stack/project/spring-boot/upload/）
    ↓
🔗 返回访问 URL（http://localhost:8083/upload/xxx.jpg）
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
| 🧹 简化 | Lombok | — | 🧹 |
| 🔥 热重载 | DevTools | — | 🔥 |
| 🧪 测试 | JUnit 5 + Mockito | — | 🧪 |

---

## 📁 项目结构

```
reg-login/
├── 📄 pom.xml                          # Maven 依赖配置
├── 📄 READER.md                        # 本文件 📖
│
├── 📂 src/main/java/com/spring/demo/reg_login/
│   ├── 🚀 RegLoginApplication.java     # Spring Boot 启动类
│   │
│   ├── 📂 config/
│   │   └── ⚙️ WebConfig.java            # CORS + 拦截器 + 静态资源
│   │
│   ├── 📂 common/
│   │   ├── 📦 Result.java              # 统一响应体 {code, message, data}
│   │   └── 📦 PageResult.java          # 分页响应体 {total, list}
│   │
│   ├── 📂 controller/
│   │   ├── 👋 HelloController.java     # 测试接口 (/hello, /parse, /info)
│   │   ├── 👤 UserController.java      # 用户接口 (/register, /login, /logout, /me)
│   │   ├── 📝 ArticleController.java   # 文章接口 (/article/*)
│   │   ├── 📤 UploadController.java    # 文件上传 (/upload)
│   │   └── 🟥 RedisController.java     # 当前用户测试 (/currentUser)
│   │
│   ├── 📂 service/
│   │   ├── 👤 UserService.java         # 注册/登录逻辑
│   │   └── 📝 ArticleService.java      # 文章 CRUD + 权限校验
│   │
│   ├── 📂 mapper/
│   │   ├── 👤 UserMapper.java          # 用户 SQL（注解式）
│   │   └── 📝 ArticleMapper.java       # 文章 SQL（注解式 + 动态SQL）
│   │
│   ├── 📂 entity/
│   │   ├── 👤 User.java                # 用户实体
│   │   └── 📝 Article.java             # 文章实体
│   │
│   ├── 📂 dto/
│   │   ├── 📥 LoginRequest.java        # 登录请求 DTO
│   │   ├── 📥 RegisterRequest.java     # 注册请求 DTO
│   │   └── 📂 article/
│   │       ├── 📥 ArticleRequest.java       # 新增文章 DTO
│   │       ├── 📥 ArticleUpdateRequest.java # 修改文章 DTO
│   │       └── 📥 ArticleDeleteRequest.java # 删除文章 DTO
│   │
│   ├── 📂 filter/
│   │   └── 🛡️ JwtAuthFilter.java       # JWT 认证过滤器（核心🔐）
│   │
│   ├── 📂 interceptor/
│   │   └── 🛡️ JwtInterceptor.java      # JWT 拦截器（备用）
│   │
│   ├── 📂 utils/
│   │   ├── 🎫 JwtUtils.java            # JWT 生成 & 解析
│   │   ├── 📁 FileUtil.java            # 文件删除工具
│   │   └── 🧵 ThreadLocalUtil.java     # 线程级用户存储
│   │
│   └── 📂 exception/
│       └── 🚨 GlobalExceptionHandler.java  # 全局异常处理
│
├── 📂 src/main/resources/
│   ├── ⚙️ application.properties       # 应用配置
│   └── 📂 sql/
│       └── 🗄️ init.sql                 # 数据库初始化脚本
│
└── 📂 src/test/java/.../
    └── 🧪 RegLoginApplicationTests.java # 测试类
```

---

## 🗄️ 数据库设计

### 📊 user 表
| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| 🔑 `id` | `BIGINT` | PK, AUTO_INCREMENT | 主键 |
| 👤 `username` | `VARCHAR(50)` | NOT NULL, UNIQUE | 用户名 |
| 🔒 `password` | `VARCHAR(100)` | NOT NULL | BCrypt 密文 |
| 🕐 `createTime` | `DATETIME` | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 📊 article 表
| 字段 | 类型 | 约束 | 说明 |
|------|------|------|------|
| 🔑 `id` | `BIGINT` | PK, AUTO_INCREMENT | 主键 |
| 📌 `title` | `VARCHAR(200)` | NOT NULL | 文章标题 |
| 📝 `content` | `TEXT` | NOT NULL | 文章内容 |
| 👤 `author` | `VARCHAR(50)` | NOT NULL | 作者（用户名） |
| 🖼️ `cover_img` | `VARCHAR(500)` | — | 封面图片 URL |
| 🕐 `createTime` | `DATETIME` | DEFAULT CURRENT_TIMESTAMP | 创建时间 |

### 🟥 Redis Key 设计
| Key | Value | TTL | 说明 |
|------|------|-----|------|
| `login:{username}` | JWT Token | 7 天 | 登录状态存储，登出时删除 |

---

## 📡 API 接口文档

> 🌐 基础路径：`http://localhost:8083`
>
> 🔐 除白名单外，所有接口需在 Header 中携带：`Authorization: <token>`

### 🟢 白名单接口（无需登录）

| 方法 | 路径 | 说明 | 请求体 |
|:----:|------|------|--------|
| 🔵 GET | `/hello` | 🧪 测试 | — |
| 🔵 GET | `/parse?token=xxx` | 🔍 JWT 解析测试 | — |
| 🟢 POST | `/register` | 📝 用户注册 | `{username, password}` |
| 🟢 POST | `/login` | 🔑 用户登录 | `{username, password}` |
| 🟢 POST | `/upload` | 📤 图片上传 | `multipart/form-data (file)` |

### 🔴 需登录接口

| 方法 | 路径 | 说明 | 请求体/参数 |
|:----:|------|------|-------------|
| 🔵 GET | `/info` | 🔐 登录用户可访问 | — |
| 🔵 GET | `/me` | 👤 获取当前用户名 | — |
| 🔵 GET | `/currentUser` | 👤 ThreadLocal 获取用户 | — |
| 🟠 POST | `/logout` | 🚪 退出登录 | — |

### 📝 文章接口（需登录）

| 方法 | 路径 | 说明 | 请求体/参数 |
|:----:|------|------|-------------|
| 🔵 GET | `/article/list` | 📋 文章列表 | — |
| 🔵 GET | `/article/detail` | 📖 文章详情 | `?id=1` |
| 🟢 POST | `/article/add` | ✍️ 发布文章 | `{title, content, coverImg}` |
| 🟠 POST | `/article/update` | ✏️ 修改文章 | `{id, title, content, coverImg}` |
| 🔴 POST | `/article/delete` | 🗑️ 删除文章 | `{id}` |
| 🔵 GET | `/article/page` | 📄 分页搜索 | `?pageNum=1&pageSize=10&title=&author=` |

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
         │  或 request.getAttribute │
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
| 🟥 Redis | 7.0+ | Token 缓存 |
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
curl http://localhost:8083/hello
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

---

<p align="center">
  🎯 <b>项目完成度：~90%</b> &nbsp;|&nbsp;
  🐙 <b>适合校招简历</b> &nbsp;|&nbsp;
  🚀 <b>持续迭代中...</b>
</p>

<p align="center">
  <sub>Made with ❤️ by Spring Boot enthusiasts</sub>
</p>
