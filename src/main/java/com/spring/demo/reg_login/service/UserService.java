package com.spring.demo.reg_login.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.spring.demo.reg_login.common.Result;
import com.spring.demo.reg_login.dto.user.UserRoleDTO;
import com.spring.demo.reg_login.dto.user.UserStatusDTO;
import com.spring.demo.reg_login.entity.User;
import com.spring.demo.reg_login.mapper.UserMapper;
import com.spring.demo.reg_login.utils.JwtUtils;
import com.spring.demo.reg_login.utils.ThreadLocalUtil;
import com.spring.demo.reg_login.vo.UserVO;

// @Service: 标记Service组件，交给Spring容器管理
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    private final StringRedisTemplate stringRedisTemplate;

    // BCrypt密码加密器：单向不可逆，每次结果不同（自带随机盐值）
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 注册
    public String register(String username, String password) {
        // 1. 判断用户名是否存在
        User existUser = userMapper.findByUsername(username);
        if (existUser != null) {
            return "用户名已存在~";
        }
        // 2. 创建用户并加密密码后存入数据库
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userMapper.insert(user);
        return "注册成功~";
    }

    // 登录
    public Result<String> login(String username, String password) {
        // 1. 根据用户名查用户
        User user = userMapper.findByUsername(username);
        if (user == null) {
            return Result.error("用户名不存在");
        }
        if (user.getStatus() == 0) {
            throw new RuntimeException("账号已被禁用");
        }
        // 2. 密码校验（明文 vs BCrypt密文）
        boolean result = passwordEncoder.matches(password, user.getPassword());
        if (!result) {
            return Result.error("密码错误");
        }
        // 3. 登录成功，生成JWT Token返回给前端
        String token = JwtUtils.generateToken(user.getUsername());
        // 4. 将Token存入Redis，设置过期时间（与JWT一致）
        stringRedisTemplate.opsForValue().set(
                "login:" + user.getUsername(),
                token,
                7,
                TimeUnit.DAYS
        );
        return Result.success(token);
    }

    // 查询所有用户（管理员页面展示用）
    public List<UserVO> list() {
        String username = ThreadLocalUtil.get();
        User currentUser = userMapper.findByUsername(username);
        if (currentUser == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!"admin".equals(currentUser.getRole())) {
            throw new RuntimeException("无权限访问");
        }
        List<User> users = userMapper.findAll();
        List<UserVO> result = new ArrayList<>();
        for (User user : users) {
            UserVO vo = new UserVO();
            vo.setId(user.getId());
            vo.setUsername(user.getUsername());
            vo.setRole(user.getRole());
            result.add(vo);
        }
        return result;
    }

    // 更新用户角色
    public void updateRole(UserRoleDTO dto) {
        String username = ThreadLocalUtil.get();
        User currentUser = userMapper.findByUsername(username);
        if (currentUser == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!"admin".equals(currentUser.getRole())) {
            throw new RuntimeException("无权限操作");
        }
        User targetUser = userMapper.findById(dto.getId());
        if (targetUser == null) {
            throw new RuntimeException("目标用户不存在");
        }
        if (!"admin".equals(dto.getRole()) && !"user".equals(dto.getRole())) {
            throw new RuntimeException("角色不合法");
        }
        userMapper.updateRole(dto.getId(), dto.getRole());
    }

    // 更新用户状态
    public void updateStatus(UserStatusDTO dto) {
        String username = ThreadLocalUtil.get();
        User currentUser = userMapper.findByUsername(username);
        if (currentUser == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!"admin".equals(currentUser.getRole())) {
            throw new RuntimeException("无权限操作");
        }
        User targetUser = userMapper.findById(dto.getId());
        if (targetUser == null) {
            throw new RuntimeException("目标用户不存在");
        }
        // 不能禁用管理员账号（status=0正常，1封禁）
        if ("admin".equals(targetUser.getUsername()) && dto.getStatus() == 0) {
            throw new RuntimeException("不能禁用管理员账号");
        }
        userMapper.updateStatus(dto.getId(), dto.getStatus());
    }

    // 分页查询用户
    public List<UserVO> page(Integer pageNum, Integer pageSize, String username) {
        // 1️⃣ 权限校验
        String loginUser = ThreadLocalUtil.get();
        User currentUser = userMapper.findByUsername(loginUser);
        if (currentUser == null) {
            throw new RuntimeException("用户不存在");
        }
        if (!"admin".equals(currentUser.getRole())) {
            throw new RuntimeException("无权限访问");
        }
        // 2️⃣ 分页（PageHelper）
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userMapper.page(username);
        // 3️⃣ 转 VO（不返回密码）
        List<UserVO> result = new ArrayList<>();
        for (User user : users) {
            UserVO vo = new UserVO();
            vo.setId(user.getId());
            vo.setUsername(user.getUsername());
            vo.setRole(user.getRole());
            result.add(vo);
        }
        return result;
    }

}
