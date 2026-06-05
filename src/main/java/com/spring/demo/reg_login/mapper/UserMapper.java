package com.spring.demo.reg_login.mapper;

import com.spring.demo.reg_login.entity.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

// @Mapper: MyBatis注解，标记Mapper接口，MyBatis用动态代理自动生成实现类
@Mapper
public interface UserMapper {

    // 根据ID查用户，#{id}是参数占位符（防SQL注入，底层用PreparedStatement的?）
    @Select("select * from user where id = #{id}")
    User findById(Long id);

    // 插入用户，返回值=受影响行数
    @Insert("""
    insert into user(username, password)
    values(#{username}, #{password})
    """)
    int insert(User user);

    // 根据用户名查询，用于注册时检查用户名是否已存在
    @Select("""
    select * from user
    where username = #{username}
    """)
    User findByUsername(String username);

}
