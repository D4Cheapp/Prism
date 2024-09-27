package com.prism.messenger.service.relation.impl;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.exception.relation.AddCurrentProfileToCurrentProfileException;
import com.prism.messenger.model.profile.QueryReceiveProfileListModel;
import com.prism.messenger.model.profile.ReceiveProfileListModel;
import com.prism.messenger.repository.ProfileRepository;
import com.prism.messenger.service.profile.impl.ProfileServiceImpl;
import com.prism.messenger.service.relation.RelationService;
import com.prism.messenger.util.ProfileUtil;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RelationServiceImpl implements RelationService {

  @Autowired
  private ProfileRepository profileRepository;
  @Autowired
  private ProfileServiceImpl profileServiceImpl;

  public void addFriend(String email, String friendTag)
      throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException {
    Profile profile = ProfileUtil.getProfileBy(email, profileRepository::findByEmail);
    checkIsUserACurrentProfile(profile, friendTag);
    profileRepository.addFriend(email, friendTag);
    profileRepository.unBlockUser(email, friendTag);
  }

  public void deleteFriend(String email, String friendTag) {
    profileRepository.deleteFriend(email, friendTag);
  }

  public void blockUser(String email, String userTag)
      throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException {
    Profile profile = ProfileUtil.getProfileBy(userTag, profileRepository::findByTag);
    checkIsUserACurrentProfile(profile, userTag);
    profileRepository.deleteFriend(email, userTag);
    profileRepository.blockUser(email, userTag);
  }

  public void unBlockUser(String email, String userTag) {
    profileRepository.unBlockUser(email, userTag);
  }

  public ReceiveProfileListModel getFriendList(String email, Integer page, Integer size)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
      NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
      XmlParserException, InternalException {
    Optional<QueryReceiveProfileListModel> friendList = profileRepository.getFriendList(email,
        page * size, size);
    return profileServiceImpl.convertToListModel(friendList);
  }

  public ReceiveProfileListModel getFriendRequestsList(String email, Integer page, Integer size)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
      NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
      XmlParserException, InternalException {
    Optional<QueryReceiveProfileListModel> friendList = profileRepository.getFriendRequestsList(
        email, page * size, size);
    return profileServiceImpl.convertToListModel(friendList);
  }

  public ReceiveProfileListModel getSentFriendRequestList(String email, Integer page,
      Integer size)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
      NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
      XmlParserException, InternalException {
    Optional<QueryReceiveProfileListModel> friendList = profileRepository.getSentFriendRequest(
        email, page * size, size);
    return profileServiceImpl.convertToListModel(friendList);
  }

  public ReceiveProfileListModel getBlockList(String email, Integer page, Integer size)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
      NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
      XmlParserException, InternalException {
    Optional<QueryReceiveProfileListModel> blockList = profileRepository.getBlockList(email,
        page * size, size);
    return profileServiceImpl.convertToListModel(blockList);
  }


  public void declineFriendRequest(String email, String tag) {
    profileRepository.declineFriendRequest(email, tag);
  }

  private void checkIsUserACurrentProfile(Profile profile, String friendTag)
      throws AddCurrentProfileToCurrentProfileException {
    boolean isRequestedUserIsACurrentProfile = friendTag.equals(profile.getTag());
    if (isRequestedUserIsACurrentProfile) {
      throw new AddCurrentProfileToCurrentProfileException();
    }
  }
}
