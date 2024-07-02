package com.prism.messenger.component;

import com.prism.messenger.model.RabbitMQMessageModel;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class RabbitMQComponent {
    @RabbitListener(queues = "prism_messenger_queue")
    public void consumeMessage(RabbitMQMessageModel message) {
        System.out.println(message.getMessage());
    }
}
