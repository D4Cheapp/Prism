package com.messenger.prism.service.user;

import java.io.File;

import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceInterface {
  @Override
  public Boolean changeIsOnline(String userID, Boolean isOnline) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changeIsOnline'");
  }

  @Override
  public Boolean changeName(String userID, String name) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changeName'");
  }

  @Override
  public Boolean changeID(String userID, String newID) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changeID'");
  }

  @Override
  public Boolean changeProfilePicture(String userID, File profilePicture) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changeProfilePicture'");
  }

  @Override
  public Boolean deleteProfilePicture(String userID) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteProfilePicture'");
  }

  @Override
  public Boolean addGroup(String userID, String chatID) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addGroup'");
  }

  @Override
  public Boolean deleteGroup(String userID, String chatID) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteGroup'");
  }
}
