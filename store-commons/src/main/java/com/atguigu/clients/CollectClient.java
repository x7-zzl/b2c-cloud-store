package com.atguigu.clients;

import com.atguigu.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * projectName: b2c-store
 * <p>
 * description: 收藏对应的客户端
 */
@FeignClient("collect-service")
public interface CollectClient {

    @PostMapping("/collect/remove/product")
    R remove(@RequestBody Integer productId);

}
