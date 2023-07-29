package com.atguigu.cart.listener;

import com.atguigu.cart.service.CartService;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * projectName: b2c-store
 * <p>
 * description: 监听mq消息
 */
@Component
public class CartRabbitMqListener {

    @Autowired
    private CartService cartService;

    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(name = "clear.queue"),
                    exchange = @Exchange(value = "topic.ex"),
                    key = "clear.cart"
            )
    )
    public void clear(List<Integer> cartIds){

        cartService.clearIds(cartIds);
    }

}
