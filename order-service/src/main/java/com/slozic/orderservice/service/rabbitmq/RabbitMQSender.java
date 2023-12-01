package com.slozic.orderservice.service.rabbitmq;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class RabbitMQSender<T> {
    private AmqpTemplate rabbitTemplate;
    private Queue queue;

    public void send(T event) {
        rabbitTemplate.convertAndSend(queue.getName(), event);
        log.info("Sending Message to the Queue : " + event.toString());
    }
}
