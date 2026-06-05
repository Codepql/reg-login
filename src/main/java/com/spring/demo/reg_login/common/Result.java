package com.spring.demo.reg_login.common;

import lombok.Data;

// 统一响应结果：所有接口返回给前端的数据都包在这一层（code + message + data）
// <T> 泛型让 data 可以是任意类型（String、User、List 等）
@Data
public class Result<T> {

    private Integer code;       // 状态码（200=成功，500=失败）

    private String message;     // 提示信息（成功="success"，失败=具体错误描述）

    private T data;             // 响应数据（泛型，可以是任何类型）

    // 成功响应（状态码200）
    public static <T> Result<T> success(T data) {

        Result<T> result = new Result<>();

        result.setCode(200);
        result.setMessage("success");
        result.setData(data);

        return result;
    }

    // 失败响应（状态码500，data为null）
    public static <T> Result<T> error(String message) {

        Result<T> result = new Result<>();

        result.setCode(500);
        result.setMessage(message);
        result.setData(null);

        return result;
    }

}
