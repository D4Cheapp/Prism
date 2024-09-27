package com.prism.messenger.service.relation;

import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.exception.relation.AddCurrentProfileToCurrentProfileException;
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

public interface RelationService {

  void addFriend(String email, String friendTag)
      throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException;

  void deleteFriend(String email, String friendTag);

  void blockUser(String email, String userTag)
      throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException;

  void unBlockUser(String email, String userTag);

  ReceiveProfileListModel getFriendList(String email, Integer page, Integer size)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
      NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
      XmlParserException, InternalException;

  ReceiveProfileListModel getBlockList(String email, Integer page, Integer size)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
      NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
      XmlParserException, InternalException;

  ReceiveProfileListModel getFriendRequestsList(String email, Integer page, Integer size)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
      NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
      XmlParserException, InternalException;

  ReceiveProfileListModel getSentFriendRequestList(String email, Integer page, Integer size)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
      NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
      XmlParserException, InternalException;

  void declineFriendRequest(String email, String tag);
}
