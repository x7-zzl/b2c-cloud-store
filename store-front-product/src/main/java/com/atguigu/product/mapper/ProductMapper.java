package com.atguigu.product.mapper;

import com.atguigu.pojo.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * projectName: b2c-store
 * <p>
 * description: 商品的mapper接口
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
