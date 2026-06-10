package com.spring.demo.reg_login.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.spring.demo.reg_login.entity.OperationLog;

@Mapper
public interface OperationLogMapper {

    // 增
    @Insert("""
        insert into operation_log(
            username,
            operation
        )
        values(
            #{username},
            #{operation}
        )
    """)
    void insert(OperationLog log);

    // 查list
    @Select("""
        select *
        from operation_log
        order by id desc
    """)
    List<OperationLog> list();
    
} 
