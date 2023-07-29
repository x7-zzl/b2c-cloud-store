package com.atguigu.user.service;

import com.atguigu.param.AddressParam;
import com.atguigu.pojo.Address;
import com.atguigu.utils.R;

/**
 * projectName: b2c_cloud_store
 *
 * @author: 赵伟风
 * time: 2022/10/17 11:15 周一
 * description:
 */
public interface AddressService {

    /**
     * 查询地址列表
     * @param userId
     * @return
     */
    R list(Integer userId);

    /**
     * 保存数据库数据
     * @param address
     * @return
     */
    R save(AddressParam address);

    /**
     * 删除地址数据
     * @param id
     * @return
     */
    R remove(Integer id);
}
