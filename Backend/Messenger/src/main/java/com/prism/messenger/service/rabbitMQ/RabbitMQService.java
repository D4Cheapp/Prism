package com.prism.messenger.service.rabbitMQ;

import com.prism.messenger.exception.CreateProfileException;
import com.prism.messenger.exception.rabbitMQ.IncorrectMessageActionException;
import com.prism.messenger.model.RabbitMQMessageModel;

public interface RabbitMQService {
    void consumeMessage(RabbitMQMessageModel message) throws CreateProfileException, IncorrectMessageActionException;

}
