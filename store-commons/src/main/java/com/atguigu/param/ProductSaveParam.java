package com.atguigu.param;

import com.atguigu.pojo.Product;
import lombok.Data;

/**
 * projectName: b2c-store
 * <p>
 * description: 商品数据保存param
 */
@Data
public class ProductSaveParam extends Product {

    /**
     * 保存商品详情的图片地址! 图片之间使用 + 拼接
     */
    private String pictures;
}
