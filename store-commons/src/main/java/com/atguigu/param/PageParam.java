package com.atguigu.param;

import lombok.Data;

/**
 * projectName: b2c-store
 * <p>
 * description: 分页的属性
 */
@Data
public class PageParam {

    private int currentPage = 1; //默认值1
    private int pageSize = 15; //默认值 15
}
