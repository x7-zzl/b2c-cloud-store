package com.atguigu.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@MapperScan(basePackages = "com.atguigu.user.mapper")
@SpringBootApplication
//开启feign的客户端,暂时不需要
@EnableFeignClients()
public class FrontUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontUserApplication.class,args);
    }

}
