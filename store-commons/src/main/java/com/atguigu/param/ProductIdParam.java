package com.atguigu.param;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * projectName: b2c-store
 * <p>
 * description: 商品id参数接收
 */
@Data
public class ProductIdParam {

    @NotNull
    private Integer productID;
}
