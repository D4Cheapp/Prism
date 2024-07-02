package com.prism.authentication.component;

import com.prism.authentication.model.RabbitMQMessageModel;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class RabbitMQComponent {
    @RabbitListener(queues = "prism_auth_queue")
    public void consumeMessage(RabbitMQMessageModel message) {
        System.out.println("Message received: " + message);
    }
}
