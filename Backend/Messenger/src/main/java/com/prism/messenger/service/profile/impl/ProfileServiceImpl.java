package com.prism.messenger.service.profile.impl;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.profile.AddCurrentProfileToCurrentProfileException;
import com.prism.messenger.exception.profile.CreateProfileException;
import com.prism.messenger.exception.profile.DeleteUserProfileException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.model.profile.*;
import com.prism.messenger.repository.ProfileRepository;
import com.prism.messenger.service.minio.impl.MinioServiceImpl;
import com.prism.messenger.service.profile.ProfileService;
import com.prism.messenger.util.ProfileUtil;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MinioServiceImpl minioService;

    public FullProfileInfoModel getCurrentProfile(String email)
            throws ProfileNotExistException, ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        Profile profile = ProfileUtil.getProfileBy(email, profileRepository::findByEmail);
        byte[] profilePhoto = minioService.loadPictureInProfileModel(profile);
        return new FullProfileInfoModel(profile, profilePhoto, null);
    }

    public FullProfileInfoModel getProfileByTag(String tag, String email)
            throws ProfileNotExistException, ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        Profile profile = ProfileUtil.getProfileBy(tag, profileRepository::findByTag);
        byte[] profilePhoto = minioService.loadPictureInProfileModel(profile);
        RelationsBetweenUserModel relationWithCurrentProfile = loadRelationsWithCurrentProfile(tag,
                email);
        return new FullProfileInfoModel(profile, profilePhoto, relationWithCurrentProfile);
    }

    public FullProfileInfoModel getProfileByTelephone(String phoneNumber, String email)
            throws ProfileNotExistException, ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        Profile profile = ProfileUtil.getProfileBy(phoneNumber,
                profileRepository::findByPhoneNumber);
        byte[] profilePhoto = minioService.loadPictureInProfileModel(profile);
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
            minioService.createFolder("profiles/" + email);
        } catch (Exception e) {
            throw new CreateProfileException();
        }
    }

    public void deleteProfile(String email) throws DeleteUserProfileException {
        try {
            Profile profile = ProfileUtil.getProfileBy(email, profileRepository::findByEmail);
            profileRepository.deleteByTag(profile.getTag());
            minioService.deleteFolder("profiles/" + email);
        } catch (Exception e) {
            throw new DeleteUserProfileException();
        }
    }

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
        return convertToListModel(friendList);
    }

    public ReceiveProfileListModel getFriendRequestsList(String email, Integer page, Integer size)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        Optional<QueryReceiveProfileListModel> friendList = profileRepository.getFriendRequestsList(
                email, page * size, size);
        return convertToListModel(friendList);
    }

    public ReceiveProfileListModel getSentFriendRequestList(String email, Integer page,
                                                            Integer size)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        Optional<QueryReceiveProfileListModel> friendList = profileRepository.getSentFriendRequest(
                email, page * size, size);
        return convertToListModel(friendList);
    }

    public ReceiveProfileListModel getBlockList(String email, Integer page, Integer size)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        Optional<QueryReceiveProfileListModel> blockList = profileRepository.getBlockList(email,
                page * size, size);
        return convertToListModel(blockList);
    }

    public ReceiveProfileListModel searchProfileByTag(String tag, Integer page, Integer size)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        Optional<QueryReceiveProfileListModel> searchList = profileRepository.searchProfileByTag(
                tag,
                page * size, size);
        return convertToListModel(searchList);
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

    private ReceiveProfileListModel convertToListModel(
            Optional<QueryReceiveProfileListModel> profileList)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        boolean isTotalCountEmpty = profileList.isPresent() && profileList.get()
                .getTotalCount() == 0;
        boolean isProfileListNotEmpty = profileList.isPresent() && !isTotalCountEmpty;
        if (isProfileListNotEmpty) {
            List<ProfileModel> ProfileModelList = new ArrayList<>();
            for (Profile profile : profileList.get().getProfiles()) {
                byte[] profilePicture = minioService.getFile(profile.getProfilePicturePath());
                ProfileModelList.add(ProfileModel.toModel(profile, profilePicture));
            }
            return new ReceiveProfileListModel(profileList.get().getTotalCount(), ProfileModelList);
        } else {
            return new ReceiveProfileListModel(0, null);
        }
    }
}
