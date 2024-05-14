package com.messenger.prism.service.auth;

import org.springframework.stereotype.Service;

import com.messenger.prism.model.Auth;

@Service
public class AuthService implements AuthServiceInterface {
  @Override
  public Boolean auth(String login, String password) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'auth'");
  }

  @Override
  public Auth getUser(String login) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getUser'");
  }

  @Override
  public Boolean registerUser(Auth auth) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'registerUser'");
  }

  @Override
  public Boolean deleteUser(String login) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteUser'");
  }

  @Override
  public Boolean changeLogin(String login, String newLogin) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changeLogin'");
  }

  @Override
  public Boolean changePassword(String login, String oldPassword, String newPassword) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changePassword'");
  }

}
