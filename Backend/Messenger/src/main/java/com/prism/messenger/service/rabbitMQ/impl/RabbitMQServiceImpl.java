package com.prism.messenger.service.rabbitMQ.impl;

import com.prism.messenger.exception.profile.ChangeProfileEmailException;
import com.prism.messenger.exception.profile.CreateProfileException;
import com.prism.messenger.exception.profile.DeleteUserProfileException;
import com.prism.messenger.exception.rabbitMQ.IncorrectMessageActionException;
import com.prism.messenger.model.rabbitMQ.RabbitMQMessageModel;
import com.prism.messenger.service.profile.impl.ChangeProfileInfoServiceImpl;
import com.prism.messenger.service.profile.impl.ProfileServiceImpl;
import com.prism.messenger.service.rabbitMQ.RabbitMQService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQServiceImpl implements RabbitMQService {

  @Autowired
  private ChangeProfileInfoServiceImpl changeProfileInfoService;
  @Autowired
  private ProfileServiceImpl profileService;

  @RabbitListener(queues = "${spring.rabbitmq.queue}")
  public void consumeMessage(RabbitMQMessageModel message)
      throws CreateProfileException, IncorrectMessageActionException,
      DeleteUserProfileException, ChangeProfileEmailException {
    String action = message.getAction();
    switch (action) {
      case "createUserProfile" -> profileService.createProfile((String) message.getMessage());
      case "deleteUserProfile" -> profileService.deleteProfile((String) message.getMessage());
      case "changeProfileEmail" ->
          changeProfileInfoService.changeProfileEmail(message.getMessage());
      default -> throw new IncorrectMessageActionException();
    }
  }
}
