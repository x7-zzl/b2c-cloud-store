package com.atguigu.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * projectName: b2c-store
 * <p>
 * description: 热门商品参数接收对象
 */
@Data
public class ProductHotParam {

    @NotEmpty
    private List<String> categoryName;
}
