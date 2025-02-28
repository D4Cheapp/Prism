package com.prism.messenger.controller;

import com.prism.messenger.exception.profile.IncorrectPhoneNumberException;
import com.prism.messenger.exception.profile.PhoneNumberAlreadyExistException;
import com.prism.messenger.exception.profile.ProfileNameIsTooLongException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.exception.profile.ProfilePictureIsEmpty;
import com.prism.messenger.exception.profile.StatusIsTooLongException;
import com.prism.messenger.exception.profile.TagAlreadyExistException;
import com.prism.messenger.model.common.PaginationListModel;
import com.prism.messenger.model.common.TextResponseModel;
import com.prism.messenger.model.profile.ChangeProfilePropertyModel;
import com.prism.messenger.model.profile.FullProfileInfoModel;
import com.prism.messenger.model.profile.ProfileTagModel;
import com.prism.messenger.model.profile.ReceiveProfileListModel;
import com.prism.messenger.service.profile.impl.ChangeProfileInfoServiceImpl;
import com.prism.messenger.service.profile.impl.ProfileServiceImpl;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  @Operation(summary = "Get current profile")
  @GetMapping
  public ResponseEntity<FullProfileInfoModel> getCurrentProfile(Authentication authentication)
      throws ProfileNotExistException, ServerException, InsufficientDataException,
      ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
      InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    FullProfileInfoModel currentProfile = profileService.getCurrentProfile(email);
    return new ResponseEntity<>(currentProfile, HttpStatus.OK);
  }

  @Operation(summary = "Get profile by tag")
  @GetMapping("/{tag}")
  public ResponseEntity<FullProfileInfoModel> getProfileByTag(@PathVariable String tag,
      Authentication authentication)
      throws ProfileNotExistException, ServerException, InsufficientDataException,
      ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
      InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    FullProfileInfoModel currentProfile = profileService.getProfileByTag(tag, email);
    return new ResponseEntity<>(currentProfile, HttpStatus.OK);
  }

  @Operation(summary = "Get profile by phone number")
  @GetMapping("/phone/{telephone}")
  public ResponseEntity<FullProfileInfoModel> searchProfileByPhoneNumber(
      @PathVariable String telephone, Authentication authentication)
      throws ProfileNotExistException, ServerException, InsufficientDataException,
      ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
      InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    FullProfileInfoModel foundedProfiles = profileService.getProfileByTelephone(telephone, email);
    return new ResponseEntity<>(foundedProfiles, HttpStatus.OK);
  }

  @Operation(summary = "Search profile by tag")
  @GetMapping("/search/{tag}")
  public ResponseEntity<ReceiveProfileListModel> searchProfileByTag(@PathVariable String tag,
      @RequestBody PaginationListModel requestPaginationListModel)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
      NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
      InternalException {
    Integer page = requestPaginationListModel.getPage();
    Integer size = requestPaginationListModel.getSize();
    ReceiveProfileListModel foundedProfiles = profileService.searchProfileByTag(tag, page, size);
    return new ResponseEntity<>(foundedProfiles, HttpStatus.OK);
  }

  @Operation(summary = "Change profile name")
  @PatchMapping("/name")
  public ResponseEntity<TextResponseModel> changeProfileEmail(
      @RequestBody ChangeProfilePropertyModel profileName, Authentication authentication)
      throws ProfileNotExistException, ProfileNameIsTooLongException {
    String email = authentication.getName();
    changeProfileInfoService.changeProfileName(email, profileName.getProperty());
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Name changed", true),
        HttpStatus.OK);
  }

  @Operation(summary = "Change profile phone")
  @PatchMapping("/phone")
  public ResponseEntity<TextResponseModel> changeProfilePhone(
      @RequestBody ChangeProfilePropertyModel profilePhone, Authentication authentication)
      throws ProfileNotExistException, PhoneNumberAlreadyExistException,
      IncorrectPhoneNumberException {
    String email = authentication.getName();
    changeProfileInfoService.changeProfilePhoneNumber(email, profilePhone.getProperty());
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Phone changed", true),
        HttpStatus.OK);
  }

  @Operation(summary = "Change profile tag")
  @PatchMapping("/tag")
  public ResponseEntity<TextResponseModel> changeProfileTag(@RequestBody ProfileTagModel profileTag,
      Authentication authentication) throws ProfileNotExistException, TagAlreadyExistException {
    String email = authentication.getName();
    changeProfileInfoService.changeProfileTag(email, profileTag.getTag());
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Tag changed", true),
        HttpStatus.OK);
  }

  @Operation(summary = "Change profile status")
  @PatchMapping("/status")
  public ResponseEntity<TextResponseModel> changeProfileStatus(
      @RequestBody ChangeProfilePropertyModel profileStatus, Authentication authentication)
      throws ProfileNotExistException, StatusIsTooLongException {
    String email = authentication.getName();
    changeProfileInfoService.changeProfileStatus(email, profileStatus.getProperty());
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Status changed", true),
        HttpStatus.OK);
  }

  @Operation(summary = "Change profile picture")
  @PatchMapping("/picture")
  public ResponseEntity<TextResponseModel> changeProfilePicture(
      @RequestParam("file") MultipartFile profilePicture, Authentication authentication)
      throws ProfileNotExistException, ProfilePictureIsEmpty, ServerException,
      InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException,
      InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    changeProfileInfoService.changeProfilePicture(email, profilePicture);
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Profile picture " + "changed", true), HttpStatus.OK);
  }


  @Operation(summary = "Delete profile picture")
  @DeleteMapping("/picture")
  public ResponseEntity<TextResponseModel> deleteProfilePicture(Authentication authentication)
      throws ProfileNotExistException, ServerException, ProfilePictureIsEmpty,
      InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException,
      InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    changeProfileInfoService.deleteProfilePicture(email);
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Profile picture " + "deleted", true), HttpStatus.OK);
  }
}
