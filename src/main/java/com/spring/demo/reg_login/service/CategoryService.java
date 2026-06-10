package com.spring.demo.reg_login.service;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.spring.demo.reg_login.entity.Category;
import com.spring.demo.reg_login.mapper.CategoryMapper;
import com.spring.demo.reg_login.utils.ThreadLocalUtil;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;

    // 查询所有分类
    public List<Category> list() {
        return categoryMapper.findAll();
    }

    // 新增分类
    public void add(String categoryName) {
        Category category = new Category();
        category.setCategoryName(categoryName);
        // 当前登录用户
        category.setCreateUser(ThreadLocalUtil.get());
        categoryMapper.insert(category);
    }

    // 修改分类
    public void update(Long id, String categoryName) {
        Category category = categoryMapper.findById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        category.setCategoryName(categoryName);
        categoryMapper.update(category);
    }

    // 删除分类
    public void delete(Long id) {
        Category category = categoryMapper.findById(id);
        if (category == null) {
            throw new RuntimeException("分类不存在");
        }
        categoryMapper.delete(id);
    }

}
