package com.prism.messenger.controller;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.profile.IncorrectPhoneNumberException;
import com.prism.messenger.exception.profile.PhoneNumberAlreadyExistException;
import com.prism.messenger.exception.profile.ProfileNameIsTooLongException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.exception.profile.StatusIsTooLongException;
import com.prism.messenger.exception.profile.TagAlreadyExistException;
import com.prism.messenger.model.profile.ChangeProfileNameModel;
import com.prism.messenger.model.profile.ChangeProfilePhoneModel;
import com.prism.messenger.model.profile.ChangeProfileStatusModel;
import com.prism.messenger.model.profile.ChangeProfileTagModel;
import com.prism.messenger.model.profile.FullProfileInfoModel;
import com.prism.messenger.model.profile.ProfileModel;
import com.prism.messenger.service.profile.impl.ChangeProfileInfoServiceImpl;
import com.prism.messenger.service.profile.impl.ProfileServiceImpl;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Profile")
@RestController
@RequestMapping("/profile")
public class ProfileController {

  @Autowired
  private ChangeProfileInfoServiceImpl changeProfileInfoService;
  @Autowired
  private ProfileServiceImpl profileService;

  @GetMapping
  public ResponseEntity<FullProfileInfoModel> getCurrentProfile(Authentication authentication)
      throws ProfileNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    FullProfileInfoModel currentProfile = profileService.getCurrentProfile(email);
    return new ResponseEntity<>(currentProfile, HttpStatus.OK);
  }

  @PatchMapping("/name")
  public ResponseEntity<ProfileModel> changeProfileEmail(
      @RequestBody ChangeProfileNameModel profileName, Authentication authentication)
      throws ProfileNotExistException, ProfileNameIsTooLongException {
    String email = authentication.getName();
    Profile profile = changeProfileInfoService.changeProfileName(email, profileName.getName());
    return new ResponseEntity<>(ProfileModel.toModel(profile), HttpStatus.OK);
  }

  @PatchMapping("/phone")
  public ResponseEntity<ProfileModel> changeProfilePhone(
      @RequestBody ChangeProfilePhoneModel profilePhone, Authentication authentication)
      throws ProfileNotExistException, PhoneNumberAlreadyExistException, IncorrectPhoneNumberException {
    String email = authentication.getName();
    Profile profile = changeProfileInfoService.changeProfilePhoneNumber(email,
        profilePhone.getPhoneNumber());
    return new ResponseEntity<>(ProfileModel.toModel(profile), HttpStatus.OK);
  }

  @PatchMapping("/tag")
  public ResponseEntity<ProfileModel> changeProfileTag(
      @RequestBody ChangeProfileTagModel profileTag, Authentication authentication)
      throws ProfileNotExistException, TagAlreadyExistException, PermissionsException {
    String email = authentication.getName();
    Profile profile = changeProfileInfoService.changeProfileTag(email, profileTag.getNewTag());
    return new ResponseEntity<>(ProfileModel.toModel(profile), HttpStatus.OK);
  }

  @PatchMapping("/status")
  public ResponseEntity<ProfileModel> changeProfileStatus(
      @RequestBody ChangeProfileStatusModel profileStatus, Authentication authentication)
      throws ProfileNotExistException, StatusIsTooLongException {
    String email = authentication.getName();
    Profile profile = changeProfileInfoService.changeProfileStatus(email,
        profileStatus.getStatus());
    return new ResponseEntity<>(ProfileModel.toModel(profile), HttpStatus.OK);
  }

  @PatchMapping("/profile-picture")
  public ResponseEntity<ProfileModel> changeProfilePicture(
      @RequestParam("file") MultipartFile profilePicture, Authentication authentication)
      throws ProfileNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    Profile profile = changeProfileInfoService.changeProfilePicture(email, profilePicture);
    return new ResponseEntity<>(ProfileModel.toModel(profile), HttpStatus.OK);
  }
}
