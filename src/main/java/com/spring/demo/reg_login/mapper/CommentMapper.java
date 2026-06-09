package com.spring.demo.reg_login.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.spring.demo.reg_login.entity.Comment;

@Mapper
public interface CommentMapper {
    // 发表评论
    @Insert("""
        insert into comment(

            article_id,

            content,

            create_user

        )
        values(

            #{articleId},

            #{content},

            #{createUser}

        )
    """)
    int insert(Comment comment);

    // 查询文章评论
    @Select("""
        select *
        from comment
        where article_id = #{articleId}
        order by id desc
    """)
    List<Comment> findByArticleId(Long articleId);

    // 根据ID查询评论
    @Select("""
        select *
        from comment
        where id = #{id}
    """)
    Comment findById(Long id);

    // 删除评论
    @Delete("""
        delete from comment
        where id = #{id}
    """)
    int deleteById(Long id);
}
