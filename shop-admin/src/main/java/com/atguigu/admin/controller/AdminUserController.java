package com.atguigu.admin.controller;

import com.atguigu.admin.param.AdminUserParam;
import com.atguigu.admin.pojo.AdminUser;
import com.atguigu.admin.service.AdminUserService;
import com.atguigu.pojo.User;
import com.atguigu.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * projectName: b2c-store
 * <p>
 * description: 后台管理用户处理controller
 */
@RestController
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;

    @PostMapping("/user/login")
    public R login(@Validated AdminUserParam adminUserParam, BindingResult result, HttpSession session){
        if (result.hasErrors()){
            return R.fail("核心参数为null,登录失败!");
        }

        //验证码校验
        String captcha = (String) session.getAttribute("captcha");

        if (!adminUserParam.getVerCode().equalsIgnoreCase(captcha)){
            return R.fail("验证码错误!");
        }

        AdminUser user =  adminUserService.login(adminUserParam);

        if (user == null){
            return R.fail("登录失败!账号或者密码错误!");
        }

        session.setAttribute("userInfo",user);

        return R.ok("登录成功!");
    }

    @GetMapping("user/logout")
    public R logout(HttpSession session){
        //清空session即可
        session.invalidate();

        return R.ok("退出登录成功!");
    }

}
