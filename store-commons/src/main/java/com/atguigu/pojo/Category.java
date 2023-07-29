package com.atguigu.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * projectName: b2c-store
 * <p>
 * description: 类别实体类
 */
@TableName("category")
@Data
public class Category {

    @JsonProperty("category_id")
    @TableId(type = IdType.AUTO)
    private Integer categoryId;
    @JsonProperty("category_name")
    private String  categoryName;
}
