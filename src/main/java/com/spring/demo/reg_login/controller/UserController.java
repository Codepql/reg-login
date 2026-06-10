package com.spring.demo.reg_login.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
// @RequiredArgsConstructor: Lombok，为final字段自动生成构造方法，Spring通过构造方法注入依赖
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.demo.reg_login.common.Result;
import com.spring.demo.reg_login.dto.LoginRequest;
import com.spring.demo.reg_login.dto.RegisterRequest;
import com.spring.demo.reg_login.dto.user.UserRoleDTO;
import com.spring.demo.reg_login.dto.user.UserStatusDTO;
import com.spring.demo.reg_login.entity.User;
import com.spring.demo.reg_login.mapper.UserMapper;
import com.spring.demo.reg_login.service.UserService;
import com.spring.demo.reg_login.utils.ThreadLocalUtil;
import com.spring.demo.reg_login.vo.UserVO;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;
    private final StringRedisTemplate redisTemplate;

    // GET /user?id=1 → 根据ID查用户
    @GetMapping("/user")
    public String getUser(@RequestParam Long id) {
        User user = userMapper.findById(id);
        if (user == null) {
            return "当前用户不存在~";
        }
        return user.toString();
    }

    // POST /register → 用户注册
    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        return userService.register(
                request.getUsername(),
                request.getPassword()
        );
    }

    // POST /login → 用户登录，返回JWT Token
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequest request) {
        return userService.login(
                request.getUsername(),
                request.getPassword()
        );
    }

    // GET /logout → 用户登出，删除Redis中的Token
    @PostMapping("/logout")
    public Result<String> logout() {
        String username = ThreadLocalUtil.get();
        redisTemplate.delete("login:" + username);
        return Result.success("退出成功");
    }

    // GET /user/list → 查询所有用户（管理员页面展示用）
    @GetMapping("/user/list")
    public Result<List<UserVO>> list() {
        return Result.success(userService.list());
    }

    // POST /user/updateRole → 更新用户角色（管理员操作）
    @PutMapping("/user/role")
    public Result<String> updateRole(@RequestBody UserRoleDTO dto) {
        userService.updateRole(dto);
        return Result.success("角色修改成功");
    }

    // POST /user/updateStatus → 更新用户状态（管理员操作）
    @PutMapping("/user/status")
    public Result<String> updateStatus(@RequestBody UserStatusDTO dto) {
        userService.updateStatus(dto);
        return Result.success("状态修改成功");
    }

    // GET /user/page → 分页查询用户
    @GetMapping("/user/page")
    public Result<List<UserVO>> page(Integer pageNum, Integer pageSize, String username) {
        return Result.success(userService.page(pageNum, pageSize, username));
    }

}
