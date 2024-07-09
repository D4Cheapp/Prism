package com.prism.messenger.model.rabbitMQ;

import lombok.Data;

@Data
public class RabbitMQChangeEmailMessageModel {

  private String oldEmail;
  private String newEmail;
}
