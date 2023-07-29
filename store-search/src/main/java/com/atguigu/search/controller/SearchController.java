package com.atguigu.search.controller;

import com.atguigu.param.ProductSearchParam;
import com.atguigu.pojo.Product;
import com.atguigu.search.service.SearchService;
import com.atguigu.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * projectName: b2c-store
 * <p>
 * description: search模块的controller
 */
@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @PostMapping("product")
    public R searchProduct(@RequestBody ProductSearchParam productSearchParam){

        return searchService.search(productSearchParam);
    }


    /**
     * 同步調用，進行商品插入！覆蓋更新的！
     * @param product
     * @return
     */
    @PostMapping("save")
    public R saveProduct(@RequestBody Product product) throws IOException {

        return searchService.save(product);
    }


    @PostMapping("remove")
    public R removeProduct(@RequestBody Integer productId) throws IOException {

        return searchService.remove(productId);
    }

}
