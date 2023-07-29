package com.atguigu.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * projectName: b2c-store
 * <p>
 * description: 购物车添加参数接收
 */
@Data
public class CartSaveParam {

    @JsonProperty("product_id")
    @NotNull
    private Integer productId;
    @JsonProperty("user_id")
    @NotNull
    private Integer userId;
}
