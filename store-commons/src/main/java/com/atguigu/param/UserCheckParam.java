package com.atguigu.param;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * projectName: b2c-store
 * <p>
 * description: 接收前端参数的param
 * TODO: 要使用jsr 303的注解 进行参数校验!
 * @NotBlank 字符串 不能为null 和 空字符串 ""
 * @NotNull  字符串 不能为null
 * @NotEmpty 集合类型 集合长度不能为0
 */
@Data
public class UserCheckParam {

    @NotBlank
    private String userName; //注意: 参数名称要等于前端传递的JSON key的名称!
}
