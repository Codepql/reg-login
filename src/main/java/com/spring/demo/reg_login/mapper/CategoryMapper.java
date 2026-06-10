package com.spring.demo.reg_login.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.spring.demo.reg_login.entity.Category;

@Mapper
public interface CategoryMapper {

    // 查list
    @Select("""
        select *
        from category
        order by id desc
    """)
    List<Category> findAll();

    // 根据id查询
    @Select("""
        select *
        from category
        where id = #{id}
    """)
    Category findById(Long id);

    // 新增分类
    @Insert("""
        insert into category(
            category_name,
            create_user
        )
        values(
            #{categoryName},
            #{createUser}
        )
    """)
    int insert(Category category);

    // 更新分类
    @Update("""
        update category
        set category_name = #{categoryName}
        where id = #{id}
    """)
    int update(Category category);

    // 删除分类
    @Delete("""
        delete from category
        where id = #{id}
    """)
    int delete(Long id);

}
