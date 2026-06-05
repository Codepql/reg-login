package com.spring.demo.reg_login.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.spring.demo.reg_login.entity.Article;

@Mapper
public interface ArticleMapper {

    // 查list
    @Select("""
        select *
        from article
        order by id desc
    """)
    List<Article> findAll();

    // 条件搜索
    @Select("""
        <script>

        select *
        from article

        <where>

            <if test="title != null and title != ''">
                and title like concat('%',#{title},'%')
            </if>

            <if test="author != null and author != ''">
                and author like concat('%',#{author},'%')
            </if>

        </where>

        order by id desc

        </script>
    """)
    List<Article> page(
            @org.apache.ibatis.annotations.Param("title")
            String title,

            @org.apache.ibatis.annotations.Param("author")
            String author
    );

    // 增
    @Insert("""
        insert into article(
            title,
            content,
            author
        )
        values(
            #{title},
            #{content},
            #{author}
        )
    """)
    int insert(Article article);

    // 查详情
    @Select("""
        select *
        from article
        where id = #{id}
    """)
    Article findById(Long id);

    // 修改
    @Update("""
        update article
        set
            title = #{title},
            content = #{content}
        where id = #{id}
    """)
    int update(Article article);

    // 删除
    @Delete("""
        delete from article
        where id = #{id}
    """)
    int deleteById(Long id);

}
