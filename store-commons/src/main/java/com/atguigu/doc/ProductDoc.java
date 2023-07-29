package com.atguigu.doc;

import com.atguigu.pojo.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * projectName: b2c-store
 * <p>
 * description: 用来存储商品搜索数据的实体类
 */
@Data
@NoArgsConstructor
public class ProductDoc extends Product {

    /**
     * 商品名称和商品标题和商品描述的综合值
     */
    private String all;

    public ProductDoc(Product product) {
       super(product.getProductId(),product.getProductName(),product.getCategoryId(),
               product.getProductTitle(),product.getProductIntro(),product.getProductPicture(),
               product.getProductPrice(),product.getProductSellingPrice(),product.getProductNum(),
               product.getProductSales());

       this.all = product.getProductName()+product.getProductTitle()+product.getProductIntro();
    }
}
