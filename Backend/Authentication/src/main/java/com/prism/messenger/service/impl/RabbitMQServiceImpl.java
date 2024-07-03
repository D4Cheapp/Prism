package com.prism.messenger.service.impl;

import com.prism.messenger.model.RabbitMQMessageModel;
import com.prism.messenger.service.RabbitMQService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@EnableRabbit
public class RabbitMQServiceImpl implements RabbitMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${spring.rabbitmq.routingkey}")
    private String routingKey;

    public void createUserProfile(String email) {
        RabbitMQMessageModel message = new RabbitMQMessageModel();
        message.setMessage(email);
        message.setAction("createUserProfile");
        rabbitTemplate.convertAndSend("prism.messenger.exchange", routingKey, message);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void consumeMessage(RabbitMQMessageModel message) {
        System.out.println("Message received: " + message);
    }
}
