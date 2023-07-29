package com.atguigu.product.controller;

import com.atguigu.param.ProductSaveParam;
import com.atguigu.pojo.Product;
import com.atguigu.product.service.ProductService;
import com.atguigu.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * projectName: b2c-store
 * <p>
 * description: 商品用于后台管理数据支持的controller
 */
@RestController
@RequestMapping("product")
public class ProductAdminController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/count")
    public Long adminCount(@RequestBody Integer categoryId){

        return productService.adminCount(categoryId);
    }

    @PostMapping("/admin/save")
    public R adminSave(@RequestBody ProductSaveParam productSaveParam){

        return productService.adminSave(productSaveParam);
    }

    @PostMapping("/admin/update")
    public R adminUpdate(@RequestBody Product product){

        return productService.adminUpdate(product);
    }

    @PostMapping("/admin/remove")
    public R adminRemove(@RequestBody Integer productId){

        return productService.adminRemove(productId);
    }
}
