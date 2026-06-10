package com.spring.demo.reg_login.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.demo.reg_login.common.Result;
import com.spring.demo.reg_login.dto.category.CategoryRequest;
import com.spring.demo.reg_login.dto.category.CategoryUpdateRequest;
import com.spring.demo.reg_login.entity.Category;
import com.spring.demo.reg_login.service.CategoryService;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 查询所有分类
    @GetMapping("/category/list")
    public Result<List<Category>> list() {
        return Result.success(categoryService.list());
    }

    // 新增分类
    @PostMapping("/category/add")
    public Result<String> add(@RequestBody CategoryRequest request) {
        categoryService.add(request.getCategoryName());
        return Result.success("新增分类成功");
    }

    // 修改分类
    @PostMapping("/category/update")
    public Result<String> update(@RequestBody CategoryUpdateRequest request) {
        categoryService.update(request.getId(), request.getCategoryName());
        return Result.success("修改分类成功");
    }

    // 删除分类
    @PostMapping("/category/delete")
    public Result<String> delete(@RequestParam Long id) {
        categoryService.delete(id);
        return Result.success("删除分类成功");
    }

}
