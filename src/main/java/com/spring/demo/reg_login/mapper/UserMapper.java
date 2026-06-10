package com.spring.demo.reg_login.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.spring.demo.reg_login.entity.User;

// @Mapper: MyBatis注解，标记Mapper接口，MyBatis用动态代理自动生成实现类
@Mapper
public interface UserMapper {

    // 根据ID查用户，#{id}是参数占位符（防SQL注入，底层用PreparedStatement的?）
    @Select("""
        select *
        from user
        where id = #{id}
    """)
    User findById(Long id);

    // 插入用户，返回值=受影响行数
    @Insert("""
        insert into user(username, password, role)
        values(#{username}, #{password}, #{role})
    """)
    int insert(User user);

    // 根据用户名查询，用于注册时检查用户名是否已存在
    @Select("""
        select * from user
        where username = #{username}
    """)
    User findByUsername(String username);

    // 统计用户总数
    @Select("""
        select count(*)
        from user
    """)
    Long count();

    //查询所有用户（管理员页面展示用）
    @Select("""
        select *
        from user
        order by id desc
    """)
    List<User> findAll();

    // 更新用户角色
    @Update("""
        update user
        set role = #{role}
        where id = #{id}
    """)
    void updateRole(@Param("id") Long id, @Param("role") String role);

    // 更新用户状态
    @Update("""
        update user
        set status = #{status}
        where id = #{id}
    """)
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    // 分页查询用户
    @Select("""
    <script>
        select *
        from user

        <where>
            <if test="username != null and username != ''">
                and username like concat('%', #{username}, '%')
            </if>
        </where>

        order by id desc
    </script>
    """)
    List<User> page(@Param("username") String username);

}
