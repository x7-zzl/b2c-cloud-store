package com.atguigu.user.controller;


import com.atguigu.param.AddressParam;
import com.atguigu.pojo.Address;
import com.atguigu.user.service.AddressService;
import com.atguigu.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/user/address")
public class FrontAddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("list")
    public R list(@RequestBody Map<String,Integer> params){
        Integer userId = params.get("user_id");
        return addressService.list(userId);
    }


    @PostMapping("save")
    public R save(@RequestBody AddressParam address){

        return addressService.save(address);
    }


    @PostMapping("remove")
    public R remove(@RequestBody Map<String,Integer> params){
        Integer id = params.get("id");
        return addressService.remove(id);
    }


}
