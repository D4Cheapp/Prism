package com.prism.messenger.service.rabbitMQ.impl;

import com.prism.messenger.exception.CreateProfileException;
import com.prism.messenger.exception.rabbitMQ.IncorrectMessageActionException;
import com.prism.messenger.model.RabbitMQMessageModel;
import com.prism.messenger.service.profile.impl.ProfileServiceImpl;
import com.prism.messenger.service.rabbitMQ.RabbitMQService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQServiceImpl implements RabbitMQService {
    @Autowired
    private ProfileServiceImpl profileService;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void consumeMessage(RabbitMQMessageModel message) throws CreateProfileException,
            IncorrectMessageActionException {
        String action = message.getAction();
        if (action.equals("createUserProfile")) {
            try {
                profileService.createProfile((String) message.getMessage());
            } catch (Exception e) {
                throw new CreateProfileException();
            }
        } else {
            throw new IncorrectMessageActionException();
        }
    }
}
