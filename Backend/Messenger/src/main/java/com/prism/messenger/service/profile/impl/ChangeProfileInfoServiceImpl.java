package com.prism.messenger.service.profile.impl;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.profile.*;
import com.prism.messenger.model.rabbitMQ.RabbitMQChangeEmailMessageModel;
import com.prism.messenger.repository.ProfileRepository;
import com.prism.messenger.service.minio.impl.MinioServiceImpl;
import com.prism.messenger.service.profile.ChangeProfileInfoService;
import com.prism.messenger.util.ProfileUtil;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
public class ChangeProfileInfoServiceImpl implements ChangeProfileInfoService {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MinioServiceImpl minioService;

    public void changeProfileEmail(Object message) throws ChangeProfileEmailException {
        try {
            RabbitMQChangeEmailMessageModel changeEmailMessage =
                    (RabbitMQChangeEmailMessageModel) message;
            String oldEmail = changeEmailMessage.getOldEmail();
            String newEmail = changeEmailMessage.getNewEmail();
            minioService.createFolder("profiles/" + newEmail);
            minioService.copyFromFolder("profiles/" + oldEmail, "profiles/" + newEmail);
            minioService.deleteFolder("profiles/" + oldEmail);
            profileRepository.changeEmail(oldEmail, newEmail);
        } catch (Exception e) {
            throw new ChangeProfileEmailException();
        }
    }

    public void changeProfileName(String email, String name)
            throws ProfileNotExistException, ProfileNameIsTooLongException {
        Profile profile = ProfileUtil.getProfileBy(email, profileRepository::findByEmail);
        boolean isNameTooLong = name.length() > 100;
        if (isNameTooLong) {
            throw new ProfileNameIsTooLongException();
        }
        profile.setName(name);
        profileRepository.changeProfileName(email, name);
    }

    public void changeProfilePhoneNumber(String email, String phoneNumber)
            throws ProfileNotExistException, PhoneNumberAlreadyExistException,
            IncorrectPhoneNumberException {
        Profile profile = ProfileUtil.getProfileBy(email, profileRepository::findByEmail);
        boolean isPhoneExist = phoneNumber.isEmpty();
        String resultPhoneNumber;
        if (isPhoneExist) {
            resultPhoneNumber = null;
        } else {
            resultPhoneNumber = phoneNumberValidation(phoneNumber);
        }
        profile.setPhoneNumber(resultPhoneNumber);
        profileRepository.changeProfilePhoneNumber(email, resultPhoneNumber);
    }

    public void changeProfileTag(String email, String newTag)
            throws ProfileNotExistException, TagAlreadyExistException {
        Profile oldProfile = ProfileUtil.getProfileBy(email, profileRepository::findByEmail);
        Optional<Profile> newProfile = profileRepository.findByTag(newTag);
        boolean isNewProfileExist = newProfile.isPresent();
        boolean isTagIncorrect = !newTag.startsWith("@");
        if (isTagIncorrect) {
            newTag = "@" + newTag;
        }
        if (isNewProfileExist) {
            throw new TagAlreadyExistException();
        }
        profileRepository.changeTag(oldProfile.getTag(), newTag);
        oldProfile.setTag(newTag);
    }

    public void changeProfileStatus(String email, String status)
            throws ProfileNotExistException, StatusIsTooLongException {
        Profile profile = ProfileUtil.getProfileBy(email, profileRepository::findByEmail);
        boolean isStatusEmpty = status == null;
        if (isStatusEmpty) {
            profile.setStatus(null);
            profileRepository.changeProfileStatus(email, null);
        } else {
            String trimmedStatus = status.replaceAll("\\s+", " ").trim();
            boolean isStatusTooLong = status.length() > 100;
            if (isStatusTooLong) {
                throw new StatusIsTooLongException();
            }
            profile.setStatus(trimmedStatus);
            profileRepository.changeProfileStatus(email, trimmedStatus);
        }
    }

    public void changeProfilePicture(String email, MultipartFile picture)
            throws ProfileNotExistException, IOException, ServerException,
            InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException,
            InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Profile profile = ProfileUtil.getProfileBy(email, profileRepository::findByEmail);
        String profilePicturePath = "profiles/" + email + "/profilePicture.jpg";
        if (picture.isEmpty()) {
            minioService.deleteFile(profilePicturePath);
            profilePicturePath = null;
        } else {
            minioService.addFile(profilePicturePath, picture);
        }
        profile.setProfilePicturePath(profilePicturePath);
        profileRepository.changeProfilePicture(email, profilePicturePath);
    }

    private String phoneNumberValidation(String phoneNumber)
            throws PhoneNumberAlreadyExistException, IncorrectPhoneNumberException {
        Optional<Profile> profile = profileRepository.findByPhoneNumber(phoneNumber);
        boolean isProfileExist = profile.isPresent();
        if (isProfileExist) {
            throw new PhoneNumberAlreadyExistException();
        }
        boolean isPhoneNumberMatches = phoneNumber.matches(
                "^\\+?\\d{1,3}[-.\\s]?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-" + ".\\s]?\\d{2}[-" + ".\\s" +
                        "]?\\d{2}$");
        if (!isPhoneNumberMatches) {
            throw new IncorrectPhoneNumberException();
        }
        phoneNumber = !phoneNumber.startsWith("+") ? phoneNumber + "+" : phoneNumber;
        phoneNumber = phoneNumber.contains("-") ? phoneNumber.replace("-", "") : phoneNumber;
        phoneNumber = phoneNumber.contains(".") ? phoneNumber.replace(".", "") : phoneNumber;
        phoneNumber = phoneNumber.contains(" ") ? phoneNumber.replace(" ", "") : phoneNumber;
        return phoneNumber;
    }
}
