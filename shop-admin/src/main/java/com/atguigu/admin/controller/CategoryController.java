package com.atguigu.admin.controller;

import com.atguigu.admin.service.CategoryService;
import com.atguigu.param.PageParam;
import com.atguigu.pojo.Category;
import com.atguigu.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * projectName: b2c-store
 * <p>
 * description: 类别controller
 */
@RestController
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("list")
    public R pageList(PageParam pageParam){

        return categoryService.pageList(pageParam);
    }

    @PostMapping("save")
    public R save(Category category){

        return categoryService.save(category);
    }

    @PostMapping("remove")
    public R remove(Integer categoryId){

        return categoryService.remove(categoryId);
    }

    @PostMapping("update")
    public R update(Category category){

        return categoryService.update(category);
    }
}
