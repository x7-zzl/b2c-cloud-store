package com.atguigu.param;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * projectName: b2c-store
 * <p>
 * description: 购物车查询接收参数
 */
@Data
public class CartListParam {

    @JsonProperty("user_id")
    @NotNull
    private Integer userId;
}
