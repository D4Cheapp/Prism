package com.prism.messenger.component;

import com.prism.messenger.exception.CreateProfileException;
import com.prism.messenger.exception.rabbitMQ.IncorrectMessageActionException;
import com.prism.messenger.model.RabbitMQMessageModel;
import com.prism.messenger.service.Profile.impl.ProfileServiceImpl;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@EnableRabbit
public class RabbitMQComponent {
    @Autowired
    private ProfileServiceImpl profileService;

    @RabbitListener(queues = "prism_messenger_queue")
    public void consumeMessage(RabbitMQMessageModel message) throws IncorrectMessageActionException, CreateProfileException {
        String action = message.getAction();
        if (action.equals("create_user_profile")) {
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
