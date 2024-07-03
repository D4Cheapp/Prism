package com.prism.messenger.service;

import com.prism.messenger.model.RabbitMQMessageModel;

public interface RabbitMQService {
    void createUserProfile(String email);

    void consumeMessage(RabbitMQMessageModel message);
}
