package com.atguigu.cart.mapper;

import com.atguigu.pojo.Cart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * projectName: b2c-store
 * <p>
 * description: 购物车业务mapper
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {
}
