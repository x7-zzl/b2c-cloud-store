package com.atguigu.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * projectName: b2c-store
 * <p>
 * description: 地址结合参数接收
 */
@Data
public class AddressListParam {

    @NotNull
    @JsonProperty("user_id")
    private Integer userId;
}
