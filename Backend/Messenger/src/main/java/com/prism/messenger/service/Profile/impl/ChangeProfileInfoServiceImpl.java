package com.prism.messenger.service.profile.impl;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.profile.ChangeProfileEmailException;
import com.prism.messenger.exception.profile.IncorrectPhoneNumberException;
import com.prism.messenger.exception.profile.PhoneNumberAlreadyExistException;
import com.prism.messenger.exception.profile.ProfileNameIsTooLongException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.exception.profile.StatusIsTooLongException;
import com.prism.messenger.exception.profile.TagAlreadyExistException;
import com.prism.messenger.model.rabbitMQ.RabbitMQChangeEmailMessageModel;
import com.prism.messenger.repository.ProfileRepository;
import com.prism.messenger.service.minio.impl.MinioServiceImpl;
import com.prism.messenger.service.profile.ChangeProfileInfoService;
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
import org.springframework.web.multipart.MultipartFile;

@Service
public class ChangeProfileInfoServiceImpl implements ChangeProfileInfoService {

  @Autowired
  private ProfileRepository profileRepository;
  @Autowired
  private MinioServiceImpl minioService;

  public void changeProfileEmail(Object message)
      throws ChangeProfileEmailException {
    try {
      RabbitMQChangeEmailMessageModel changeEmailMessage = (RabbitMQChangeEmailMessageModel) message;
      String oldEmail = changeEmailMessage.getOldEmail();
      String newEmail = changeEmailMessage.getNewEmail();
      minioService.createFolder("profiles/" + newEmail + "/");
      minioService.copyFromFolder("profiles/" + oldEmail + "/", "profiles/" + newEmail + "/");
      minioService.deleteFolder("profiles/" + oldEmail + "/");
      profileRepository.changeEmail(oldEmail, newEmail);
    } catch (Exception e) {
      throw new ChangeProfileEmailException();
    }
  }

  public Profile changeProfileName(String email, String name)
      throws ProfileNotExistException, ProfileNameIsTooLongException {
    Optional<Profile> profile = profileRepository.findByEmail(email);
    boolean isProfileNotFound = profile.isEmpty();
    boolean isNameTooLong = name.length() > 100;
    if (isNameTooLong) {
      throw new ProfileNameIsTooLongException();
    }
    if (isProfileNotFound) {
      throw new ProfileNotExistException();
    }
    profile.get().setName(name);
    profileRepository.changeProfileName(email, name);
    return profile.get();
  }

  public Profile changeProfilePhoneNumber(String email, String phoneNumber)
      throws ProfileNotExistException, PhoneNumberAlreadyExistException, IncorrectPhoneNumberException {
    Optional<Profile> profile = profileRepository.findByEmail(email);
    boolean isProfileNotFound = profile.isEmpty();
    boolean isPhoneExist = phoneNumber.isEmpty();
    if (isProfileNotFound) {
      throw new ProfileNotExistException();
    }
    String resultPhoneNumber;
    if (isPhoneExist) {
      resultPhoneNumber = null;
    } else {
      resultPhoneNumber = phoneNumberValidation(phoneNumber);
    }
    profile.get().setPhoneNumber(resultPhoneNumber);
    profileRepository.changeProfilePhoneNumber(email, resultPhoneNumber);
    return profile.get();
  }

  public Profile changeProfileTag(String email, String newTag)
      throws ProfileNotExistException, TagAlreadyExistException, PermissionsException {
    Optional<Profile> oldProfile = profileRepository.findByEmail(email);
    Optional<Profile> newProfile = profileRepository.findById(newTag);
    boolean isOldProfileNotFound = oldProfile.isEmpty();
    boolean isNewProfileExist = newProfile.isPresent();
    boolean isTagInorrect = !newTag.startsWith("@");
    if (isTagInorrect) {
      newTag = "@" + newTag;
    }
    if (isNewProfileExist) {
      throw new TagAlreadyExistException();
    }
    if (isOldProfileNotFound) {
      throw new ProfileNotExistException();
    }
    boolean isIncorrectPermission = !oldProfile.get().getEmail().equals(email);
    if (isIncorrectPermission) {
      throw new PermissionsException();
    }
    profileRepository.changeTag(oldProfile.get().getTag(), newTag);
    oldProfile.get().setTag(newTag);
    return oldProfile.get();
  }

  public Profile changeProfileStatus(String email, String status)
      throws ProfileNotExistException, StatusIsTooLongException {
    Optional<Profile> profile = profileRepository.findByEmail(email);
    boolean isProfileNotFound = profile.isEmpty();
    boolean isStatusTooLong = status.length() > 100;
    boolean isStatusEmpty = status.isEmpty();
    if (isProfileNotFound) {
      throw new ProfileNotExistException();
    }
    if (isStatusTooLong) {
      throw new StatusIsTooLongException();
    }
    profile.get().setStatus(status);
    profileRepository.changeProfileStatus(email, status);
    return profile.get();
  }

  public Profile changeProfilePicture(String email, MultipartFile picture)
      throws ProfileNotExistException, IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    Optional<Profile> profile = profileRepository.findByEmail(email);
    boolean isProfileNotFound = profile.isEmpty();
    if (isProfileNotFound) {
      throw new ProfileNotExistException();
    }
    String profilePicturePath = "profiles/" + email + "/profilePicture.jpg";
    if (picture.isEmpty()) {
      minioService.deleteFile(profilePicturePath);
      profilePicturePath = null;
    } else {
      minioService.addFile(profilePicturePath, picture);
    }
    profile.get().setProfilePicturePath(profilePicturePath);
    profileRepository.changeProfilePicture(email, profilePicturePath);
    return profile.get();
  }

  private String phoneNumberValidation(String phoneNumber)
      throws PhoneNumberAlreadyExistException, IncorrectPhoneNumberException {
    Optional<Profile> profile = profileRepository.findByPhoneNumber(phoneNumber);
    boolean isProfileExist = profile.isPresent();
    if (isProfileExist) {
      throw new PhoneNumberAlreadyExistException();
    }
    boolean isPhoneNumberMatches = phoneNumber.matches(
        "^\\+?\\d{1,3}[-.\\s]?\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-" + ".\\s]?\\d{2}[-" + ".\\s]?\\d{2}$");
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
