package com.spring.demo.reg_login.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.spring.demo.reg_login.common.Result;

@RestController
public class UploadController {
    @Value("${file.upload-path}")
    private String uploadPath;

    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) throws IOException {

        // 原文件名
        String originalFilename = file.getOriginalFilename();

        // 后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 新文件名
        String fileName = UUID.randomUUID() + suffix;

        // 创建目录
        File dir = new File(uploadPath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 保存文件
        file.transferTo(new File(uploadPath + fileName)
        );

        return Result.success(
                "http://localhost:8083/upload/" + fileName
        );
    }
}
