package com.spring.demo.reg_login.utils;

import java.io.File;

public class FileUtil {

    public static void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return;
        }
        String prefix = "http://localhost:8080/upload/";
        String fileName = fileUrl.replace(prefix, "");
        File file = new File("D:/Full_Stack/project/spring-boot/upload/" + fileName);
        if (file.exists()) {
            file.delete();
        }
    }

}
