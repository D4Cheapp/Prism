package com.prism.messenger.service.profile;

import com.prism.messenger.exception.profile.AddCurrentProfileToCurrentProfileException;
import com.prism.messenger.exception.profile.CreateProfileException;
import com.prism.messenger.exception.profile.DeleteUserProfileException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.model.profile.FullProfileInfoModel;
import com.prism.messenger.model.profile.RecieveProfileListModel;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface ProfileService {

  FullProfileInfoModel getCurrentProfile(String email)
      throws ProfileNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

  FullProfileInfoModel getProfileByTag(String tag, String email)
      throws ProfileNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

  void createProfile(String email) throws CreateProfileException;

  void deleteProfile(String email) throws DeleteUserProfileException;

  void addFriend(String email, String friendTag)
      throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException;

  void deleteFriend(String email, String friendTag)
      throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException;

  void blockUser(String email, String userTag)
      throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException;

  void unBlockUser(String email, String userTag) throws ProfileNotExistException;

  RecieveProfileListModel getFriendList(String email, Integer page, Integer size);

  RecieveProfileListModel getBlockList(String email, Integer page, Integer size);

  RecieveProfileListModel getFriendRequestsList(String email, Integer page, Integer size);

  RecieveProfileListModel getSendedFriendRequestList(String email, Integer page, Integer size);

  void declineFriendRequest(String email, String tag);
}
