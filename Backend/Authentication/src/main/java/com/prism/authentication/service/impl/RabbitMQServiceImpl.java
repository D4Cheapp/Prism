package com.prism.authentication.service.impl;

import com.prism.authentication.model.RabbitMQMessageModel;
import com.prism.authentication.service.RabbitMQService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQServiceImpl implements RabbitMQService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void createUserProfile(String email) {
        RabbitMQMessageModel message = new RabbitMQMessageModel();
        message.setMessage(email);
        message.setAction("create_user_profile");
        rabbitTemplate.convertAndSend("prism_messenger_exchange", "prism_routingkey", message);
    }
}
