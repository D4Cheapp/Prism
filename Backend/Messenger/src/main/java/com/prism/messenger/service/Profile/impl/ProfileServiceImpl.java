package com.prism.messenger.service.Profile.impl;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.profile.AddCurrentProfileToCurrentProfileException;
import com.prism.messenger.exception.profile.CreateProfileException;
import com.prism.messenger.exception.profile.DeleteUserProfileException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.model.profile.FullProfileInfoModel;
import com.prism.messenger.model.profile.ProfileModel;
import com.prism.messenger.model.profile.RecieveProfileListModel;
import com.prism.messenger.model.profile.RelationsBetweenUserModel;
import com.prism.messenger.repository.ProfileRepository;
import com.prism.messenger.service.Profile.ProfileService;
import com.prism.messenger.service.minio.impl.MinioServiceImpl;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileServiceImpl implements ProfileService {

  @Autowired
  private ProfileRepository profileRepository;
  @Autowired
  private MinioServiceImpl minioService;

  public FullProfileInfoModel getCurrentProfile(String email)
      throws ProfileNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    Profile profile = ProfileUtil.getProfileByEmail(email, profileRepository);
    byte[] profilePhoto = loadPictureInProfileModel(profile);
    return new FullProfileInfoModel(profile, profilePhoto, null);
  }

  public FullProfileInfoModel getProfileByTag(String tag, String email)
      throws ProfileNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    Profile profile = ProfileUtil.getProfileByTag(tag, profileRepository);
    byte[] profilePhoto = loadPictureInProfileModel(profile);
    RelationsBetweenUserModel relationWithCurrentProfile = loadRelationsWithCurrentProfile(tag,
        email);
    return new FullProfileInfoModel(profile, profilePhoto, relationWithCurrentProfile);
  }

  public FullProfileInfoModel getProfileByTelephone(String telephone, String email)
      throws ProfileNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    Profile profile = ProfileUtil.getProfileByPhone(telephone, profileRepository);
    byte[] profilePhoto = loadPictureInProfileModel(profile);
    RelationsBetweenUserModel relationWithCurrentProfile = loadRelationsWithCurrentProfile(
        profile.getTag(), email);
    return new FullProfileInfoModel(profile, profilePhoto, relationWithCurrentProfile);
  }

  public void createProfile(String email) throws CreateProfileException {
    try {
      Profile profile = new Profile();
      profile.setEmail(email);
      profile.setTag("@user" + email.hashCode());
      profile.setName("username" + email.hashCode());
      profile.setLastOnlineTime(new Date().getTime());
      profileRepository.save(profile);
      minioService.createFolder("profiles/" + email + "/");
    } catch (Exception e) {
      throw new CreateProfileException();
    }
  }

  public void deleteProfile(String email) throws DeleteUserProfileException {
    try {
      Profile profile = ProfileUtil.getProfileByEmail(email, profileRepository);
      profileRepository.deleteByTag(profile.getTag());
      minioService.deleteFolder("profiles/" + email + "/");
    } catch (Exception e) {
      throw new DeleteUserProfileException();
    }
  }

  public void addFriend(String email, String friendTag)
      throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException {
    Profile profile = ProfileUtil.getProfileByEmail(email, profileRepository);
    checkIsUserACurrentProfile(profile, friendTag);
    profileRepository.addFriend(email, friendTag);
    profileRepository.unBlockUser(email, friendTag);
  }

  public void deleteFriend(String email, String friendTag) throws ProfileNotExistException {
    Profile profile = ProfileUtil.getProfileByEmail(email, profileRepository);
    profileRepository.deleteFriend(email, friendTag);
  }

  public void blockUser(String email, String userTag)
      throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException {
    Profile profile = ProfileUtil.getProfileByTag(userTag, profileRepository);
    checkIsUserACurrentProfile(profile, userTag);
    profileRepository.deleteFriend(email, userTag);
    profileRepository.blockUser(email, userTag);
  }

  public void unBlockUser(String email, String userTag) throws ProfileNotExistException {
    Profile profile = ProfileUtil.getProfileByTag(userTag, profileRepository);
    profileRepository.unBlockUser(email, userTag);
  }

  public RecieveProfileListModel getFriendList(String email, Integer page, Integer size) {
    Optional<Integer> totalCount = profileRepository.getFriendsCount(email);
    Optional<List<Profile>> friendList = profileRepository.getFriendList(email, page * size, size);
    return convertToListModel(friendList, totalCount);
  }

  public RecieveProfileListModel getFriendRequestsList(String email, Integer page, Integer size) {
    Optional<Integer> totalCount = profileRepository.getFriendRequestsCount(email);
    Optional<List<Profile>> friendList = profileRepository.getFriendRequestsList(email, page * size,
        size);
    return convertToListModel(friendList, totalCount);
  }

  public RecieveProfileListModel getSendedFriendRequestList(String email, Integer page,
      Integer size) {
    Optional<Integer> totalCount = profileRepository.getSendedFriendRequestCount(email);
    Optional<List<Profile>> friendList = profileRepository.getSendedFriendRequest(email,
        page * size, size);
    return convertToListModel(friendList, totalCount);
  }

  public RecieveProfileListModel getBlockList(String email, Integer page, Integer size) {
    Optional<Integer> totalCount = profileRepository.getBlockListCount(email);
    Optional<List<Profile>> blockList = profileRepository.getBlockList(email, page * size, size);
    return convertToListModel(blockList, totalCount);
  }

  public RecieveProfileListModel searchProfileByTag(String tag, Integer page, Integer size) {
    Optional<Integer> totalCount = profileRepository.getSearchProfileByTagCount(tag);
    Optional<List<Profile>> searchList = profileRepository.searchProfileByTag(tag, page, size);
    return convertToListModel(searchList, totalCount);
  }

  public void declineFriendRequest(String email, String tag) {
    profileRepository.declineFriendRequest(email, tag);
  }

  public void setOnlineConnectedStatus(String email) {
    profileRepository.setIsOnlineStatus(email, true);
  }

  public void setOnlineDisconnectedStatus(String email) {
    long lastOnlineTime = new Date().getTime();
    profileRepository.setIsOnlineStatus(email, false);
    profileRepository.setLastOnlineTime(email, lastOnlineTime);
  }

  private void checkIsUserACurrentProfile(Profile profile, String friendTag)
      throws AddCurrentProfileToCurrentProfileException {
    boolean isRequestedUserIsACurrentProfile = friendTag.equals(profile.getTag());
    if (isRequestedUserIsACurrentProfile) {
      throw new AddCurrentProfileToCurrentProfileException();
    }
  }

  private RelationsBetweenUserModel loadRelationsWithCurrentProfile(String tag, String email) {
    Optional<String> relationsToUser = profileRepository.getRelationToUser(email, tag);
    Optional<String> relationsFromUser = profileRepository.getRelationFromUser(email, tag);
    return new RelationsBetweenUserModel(relationsToUser.orElse(null),
        relationsFromUser.orElse(null));
  }

  private byte[] loadPictureInProfileModel(Profile profile)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    boolean isProfilePictureNotFound = profile.getProfilePicturePath() == null;
    if (isProfilePictureNotFound) {
      return null;
    }
    return minioService.getFile(profile.getProfilePicturePath());
  }

  private RecieveProfileListModel convertToListModel(Optional<List<Profile>> profileList,
      Optional<Integer> totalCount) {
    boolean isTotalCountEmpty = (totalCount.isPresent() && totalCount.get() == 0);
    boolean isProfileListNotEmpty =
        totalCount.isPresent() && profileList.isPresent() && !isTotalCountEmpty;
    if (isProfileListNotEmpty) {
      List<ProfileModel> ProfileModelList = new ArrayList<>();
      for (Profile profile : profileList.get()) {
        ProfileModelList.add(ProfileModel.toModel(profile));
      }
      return new RecieveProfileListModel(totalCount.get(), ProfileModelList);
    } else {
      return new RecieveProfileListModel(0, null);
    }
  }
}
