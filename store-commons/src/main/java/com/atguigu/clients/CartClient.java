package com.atguigu.clients;

import com.atguigu.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * projectName: b2c-store
 * <p>
 * description: 购物车调用的feign客户端
 */
@FeignClient("cart-service")
public interface CartClient {

    @PostMapping("/cart/remove/check")
    R check(@RequestBody Integer productId);
}
