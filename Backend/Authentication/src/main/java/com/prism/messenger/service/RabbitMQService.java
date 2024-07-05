package com.prism.messenger.service;

public interface RabbitMQService {

  void createUserProfile(String email);

  void deleteUserProfile(String email);

  void changeProfileEmail(String oldEmail, String newEmail);
}
