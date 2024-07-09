package com.prism.messenger.service.rabbitMQ;

import com.prism.messenger.exception.profile.ChangeProfileEmailException;
import com.prism.messenger.exception.profile.CreateProfileException;
import com.prism.messenger.exception.profile.DeleteUserProfileException;
import com.prism.messenger.exception.rabbitMQ.IncorrectMessageActionException;
import com.prism.messenger.model.rabbitMQ.RabbitMQMessageModel;

public interface RabbitMQService {

  void consumeMessage(RabbitMQMessageModel message)
      throws CreateProfileException, IncorrectMessageActionException, DeleteUserProfileException, ChangeProfileEmailException;

}
