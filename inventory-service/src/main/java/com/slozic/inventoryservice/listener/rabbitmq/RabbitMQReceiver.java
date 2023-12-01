package com.slozic.inventoryservice.listener.rabbitmq;

import com.slozic.inventoryservice.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "${rabbitmq.queue}", id = "listener")
public class RabbitMQReceiver {

    @RabbitHandler
    public void receiver(final OrderPlacedEvent orderPlacedEvent) {
        log.info("Order listener invoked - Consuming Message with Order Identifier : " + orderPlacedEvent.getOrderNumber());
    }
}
