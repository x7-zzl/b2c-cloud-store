package com.atguigu.admin.param;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * projectName: b2c-store
 * <p>
 * description: 接收登录信息的param
 */
@Data
public class AdminUserParam {

    @Length(min = 6)
    private String userAccount; //账号
    @Length(min = 6)
    private String userPassword; //密码
    @NotBlank
    private String verCode;  //验证码

}