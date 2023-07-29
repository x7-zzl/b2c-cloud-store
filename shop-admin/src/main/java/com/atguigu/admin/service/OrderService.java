package com.atguigu.admin.service;

import com.atguigu.param.PageParam;
import com.atguigu.utils.R;

/**
 * projectName: b2c-store
 * <p>
 * description:
 */
public interface OrderService {

    /**
     * 查询订单数据
     * @param pageParam
     * @return
     */
    R list(PageParam pageParam);
}
