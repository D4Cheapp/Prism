package com.messenger.prism.service.user;

import java.io.File;

public interface UserServiceInterface {
  Boolean changeIsOnline(String userID, Boolean isOnline);

  Boolean changeName(String userID, String name);

  Boolean changeID(String userID, String newID);

  Boolean changeProfilePicture(String userID, File profilePicture);

  Boolean deleteProfilePicture(String userID);

  Boolean addGroup(String userID, String chatID);

  Boolean deleteGroup(String userID, String chatID);
}
