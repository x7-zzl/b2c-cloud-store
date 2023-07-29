package com.atguigu.category.mapper;

import com.atguigu.pojo.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * projectName: b2c-store
 * <p>
 * description: 数据库查询接口
 */
@Mapper
public interface CategoryMapper  extends BaseMapper<Category> {
}
