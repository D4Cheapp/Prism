package com.messenger.prism.service.auth;

import com.messenger.prism.model.Auth;

public interface AuthServiceInterface {
  Boolean auth(String login, String password);

  Auth getUser(String login);

  Boolean registerUser(Auth auth);

  Boolean deleteUser(String login);

  Boolean changeLogin(String login, String newLogin);

  Boolean changePassword(String login, String oldPassword, String newPassword);
}
