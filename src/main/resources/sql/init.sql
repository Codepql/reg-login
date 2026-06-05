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
    `id`         BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键',
    `username`   VARCHAR(50)  NOT NULL                 COMMENT '用户名',
    `password`   VARCHAR(100) NOT NULL                 COMMENT '密码（BCrypt密文）',
    `createTime` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 3. 文章表
CREATE TABLE IF NOT EXISTS `article` (
    `id`         BIGINT       NOT NULL AUTO_INCREMENT  COMMENT '主键',
    `title`      VARCHAR(200) NOT NULL                 COMMENT '文章标题',
    `content`    TEXT         NOT NULL                 COMMENT '文章内容',
    `author`     VARCHAR(50)  NOT NULL                 COMMENT '作者（用户名）',
    `createTime` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
