-- ============================================
-- 初始化脚本：在新电脑上执行此文件即可建库建表
-- 执行方式：mysql -u root -p < init.sql
-- ============================================

-- 1. 建库
CREATE DATABASE IF NOT EXISTS `reg_login`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE `reg_login`;

-- 2. 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键',
    `username`    VARCHAR(50)  NOT NULL                 COMMENT '用户名',
    `password`    VARCHAR(100) NOT NULL                 COMMENT '密码（BCrypt密文）',
    `role`        VARCHAR(20)  NOT NULL DEFAULT 'user'  COMMENT '角色（user: 普通用户, admin: 管理员）',
    `status`      INT          NOT NULL DEFAULT 1       COMMENT '状态（1: 正常, 0: 封禁）',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 3. 分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id`            BIGINT      NOT NULL AUTO_INCREMENT  COMMENT '主键',
    `category_name` VARCHAR(50) NOT NULL                 COMMENT '分类名称',
    `create_user`   VARCHAR(50) NOT NULL                 COMMENT '创建人（用户名）',
    `create_time`   DATETIME    DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_category_name` (`category_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 4. 文章表
CREATE TABLE IF NOT EXISTS `article` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键',
    `title`       VARCHAR(200) NOT NULL                 COMMENT '文章标题',
    `content`     TEXT         NOT NULL                 COMMENT '文章内容',
    `author`      VARCHAR(50)  NOT NULL                 COMMENT '作者（用户名）',
    `cover_img`   VARCHAR(500) DEFAULT NULL             COMMENT '封面图片URL',
    `category_id` BIGINT       DEFAULT NULL             COMMENT '分类ID',
    `like_count`  BIGINT       NOT NULL DEFAULT 0       COMMENT '点赞数',
    `view_count`  BIGINT       NOT NULL DEFAULT 0       COMMENT '浏览量',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_author` (`author`),
    KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 5. 评论表
CREATE TABLE IF NOT EXISTS `comment` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键',
    `article_id`  BIGINT       NOT NULL                 COMMENT '文章ID',
    `content`     TEXT         NOT NULL                 COMMENT '评论内容',
    `create_user` VARCHAR(50)  NOT NULL                 COMMENT '评论人（用户名）',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_article_id` (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
