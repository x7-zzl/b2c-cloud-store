package com.atguigu.param;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * projectName: b2c-store
 * <p>
 * description: 类别商品展示
 */
@Data
public class ProductIdsParam  extends PageParam{

    @NotNull
    private List<Integer> categoryID;
}
