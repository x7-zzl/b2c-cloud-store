package com.atguigu.param;

import com.atguigu.pojo.Address;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * projectName: b2c-store
 * <p>
 * description: 地址接收值的param
 */
@Data
public class AddressParam {

    @NotNull
    @JsonProperty("user_id")
    private Integer userId;

    private Address add;
}
