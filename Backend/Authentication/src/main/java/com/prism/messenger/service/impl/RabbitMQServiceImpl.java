package com.prism.messenger.service.impl;

import com.prism.messenger.model.rabbitMQ.RabbitMQChangeEmailMessageModel;
import com.prism.messenger.model.rabbitMQ.RabbitMQMessageModel;
import com.prism.messenger.service.RabbitMQService;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
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

  public void deleteUserProfile(String email) {
    RabbitMQMessageModel message = new RabbitMQMessageModel();
    message.setMessage(email);
    message.setAction("deleteUserProfile");
    rabbitTemplate.convertAndSend("prism.messenger.exchange", routingKey, message);
  }

  public void changeProfileEmail(String oldEmail, String newEmail) {
    RabbitMQChangeEmailMessageModel emails = new RabbitMQChangeEmailMessageModel();
    emails.setOldEmail(oldEmail);
    emails.setNewEmail(newEmail);
    RabbitMQMessageModel message = new RabbitMQMessageModel();
    message.setAction("changeProfileEmail");
    message.setMessage(emails);
    rabbitTemplate.convertAndSend("prism.messenger.exchange", routingKey, message);
  }
}
