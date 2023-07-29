package com.atguigu.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * projectName: b2c-store
 * <p>
 * description: 收藏调用商品传递的参数
 */
@Data
public class ProductCollectParam {

    @NotEmpty
    private List<Integer> productIds;
}
