package com.prism.messenger.service.profile;

import com.prism.messenger.exception.profile.CreateProfileException;
import com.prism.messenger.exception.profile.DeleteUserProfileException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.model.profile.FullProfileInfoModel;
import com.prism.messenger.model.profile.ReceiveProfileListModel;
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
      throws ProfileNotExistException, ServerException, InsufficientDataException,
      ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
      InvalidResponseException, XmlParserException, InternalException;

  FullProfileInfoModel getProfileByTag(String tag, String email)
      throws ProfileNotExistException, ServerException, InsufficientDataException,
      ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
      InvalidResponseException, XmlParserException, InternalException;

  FullProfileInfoModel getProfileByTelephone(String telephone, String email)
      throws ProfileNotExistException, ServerException, InsufficientDataException,
      ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
      InvalidResponseException, XmlParserException, InternalException;

  void createProfile(String email) throws CreateProfileException;

  void deleteProfile(String email) throws DeleteUserProfileException;

  void setOnlineConnectedStatus(String email);

  void setOnlineDisconnectedStatus(String email);

  ReceiveProfileListModel searchProfileByTag(String tag, Integer page, Integer size)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
      NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
      XmlParserException, InternalException;
}
