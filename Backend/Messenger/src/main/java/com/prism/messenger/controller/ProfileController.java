package com.prism.messenger.controller;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.EmptyParameterException;
import com.prism.messenger.exception.profile.AddCurrentProfileToCurrentProfileException;
import com.prism.messenger.exception.profile.IncorrectPhoneNumberException;
import com.prism.messenger.exception.profile.PhoneNumberAlreadyExistException;
import com.prism.messenger.exception.profile.ProfileNameIsTooLongException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.exception.profile.StatusIsTooLongException;
import com.prism.messenger.exception.profile.TagAlreadyExistException;
import com.prism.messenger.model.TextResponseModel;
import com.prism.messenger.model.profile.FullProfileInfoModel;
import com.prism.messenger.model.profile.ProfileModel;
import com.prism.messenger.model.profile.ReceiveProfileListModel;
import com.prism.messenger.model.profile.RequestProfileListModel;
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
import org.springframework.web.bind.annotation.PostMapping;
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
      throws ProfileNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    FullProfileInfoModel currentProfile = profileService.getCurrentProfile(email);
    return new ResponseEntity<>(currentProfile, HttpStatus.OK);
  }

  @Operation(summary = "Get profile by tag")
  @GetMapping("/{tag}")
  public ResponseEntity<FullProfileInfoModel> getProfileByTag(@PathVariable String tag,
      Authentication authentication)
      throws ProfileNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    FullProfileInfoModel currentProfile = profileService.getProfileByTag(tag, email);
    return new ResponseEntity<>(currentProfile, HttpStatus.OK);
  }

  @Operation(summary = "Get profile by phone number")
  @GetMapping("/phone/{telephone}")
  public ResponseEntity<FullProfileInfoModel> searchProfileByPhoneNumber(
      @PathVariable String telephone, Authentication authentication)
      throws ProfileNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    FullProfileInfoModel foundedProfiles = profileService.getProfileByTelephone(telephone, email);
    return new ResponseEntity<>(foundedProfiles, HttpStatus.OK);
  }

  @Operation(summary = "Search profile by tag")
  @GetMapping("/search/{tag}")
  public ResponseEntity<ReceiveProfileListModel> searchProfileByTag(@PathVariable String tag,
      @RequestBody RequestProfileListModel requestProfileListModel) {
    Integer page = requestProfileListModel.getPage();
    Integer size = requestProfileListModel.getSize();
    ReceiveProfileListModel foundedProfiles = profileService.searchProfileByTag(tag, page, size);
    return new ResponseEntity<>(foundedProfiles, HttpStatus.OK);
  }

  @Operation(summary = "Add friend")
  @PostMapping("/friend")
  public ResponseEntity<TextResponseModel> addFriend(@RequestParam("friendTag") String friendTag,
      Authentication authentication)
      throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException {
    String email = authentication.getName();
    profileService.addFriend(email, friendTag);
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Friend was added", true),
        HttpStatus.OK);
  }

  @Operation(summary = "Delete friend")
  @DeleteMapping("/friend")
  public ResponseEntity<TextResponseModel> deleteFriend(@RequestParam("friendTag") String friendTag,
      Authentication authentication) {
    String email = authentication.getName();
    profileService.deleteFriend(email, friendTag);
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Friend deleted", true),
        HttpStatus.OK);
  }

  @Operation(summary = "Get friend profile list ")
  @GetMapping("/friend-list")
  public ResponseEntity<ReceiveProfileListModel> getFriendList(
      @RequestBody RequestProfileListModel requestProfileListModel, Authentication authentication) {
    String email = authentication.getName();
    Integer page = requestProfileListModel.getPage();
    Integer size = requestProfileListModel.getSize();
    ReceiveProfileListModel currentProfile = profileService.getFriendList(email, page, size);
    return new ResponseEntity<>(currentProfile, HttpStatus.OK);
  }

  @Operation(summary = "Get sent friend request list ")
  @GetMapping("/sent-friend-requests")
  public ResponseEntity<ReceiveProfileListModel> getSentFriendRequestList(
      @RequestBody RequestProfileListModel requestProfileListModel, Authentication authentication) {
    String email = authentication.getName();
    Integer page = requestProfileListModel.getPage();
    Integer size = requestProfileListModel.getSize();
    ReceiveProfileListModel sentRequests = profileService.getSentFriendRequestList(email, page,
        size);
    return new ResponseEntity<>(sentRequests, HttpStatus.OK);
  }

  @Operation(summary = "Decline friend request")
  @PostMapping("/friend-decline")
  public ResponseEntity<TextResponseModel> friendRequestDecline(
      @RequestParam("friendTag") String friendTag,
      Authentication authentication) {
    String email = authentication.getName();
    profileService.declineFriendRequest(email, friendTag);
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Friend request was declined", true), HttpStatus.OK);
  }

  @Operation(summary = "Get friend request list ")
  @GetMapping("/friend-requests")
  public ResponseEntity<ReceiveProfileListModel> getFriendRequestList(
      @RequestBody RequestProfileListModel requestProfileListModel, Authentication authentication) {
    String email = authentication.getName();
    Integer page = requestProfileListModel.getPage();
    Integer size = requestProfileListModel.getSize();
    ReceiveProfileListModel friendRequests = profileService.getFriendRequestsList(email, page,
        size);
    return new ResponseEntity<>(friendRequests, HttpStatus.OK);
  }

  @Operation(summary = "Block user")
  @PostMapping("/block")
  public ResponseEntity<TextResponseModel> blockUser(@RequestParam("userTag") String userTag,
      Authentication authentication)
      throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException {
    String email = authentication.getName();
    profileService.blockUser(email, userTag);
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("User blocked", true),
        HttpStatus.OK);
  }

  @Operation(summary = "Get blocked profile list ")
  @GetMapping("/block-list")
  public ResponseEntity<ReceiveProfileListModel> getBlockList(
      @RequestBody RequestProfileListModel requestProfileListModel, Authentication authentication) {
    String email = authentication.getName();
    Integer page = requestProfileListModel.getPage();
    Integer size = requestProfileListModel.getSize();
    ReceiveProfileListModel profileBockList = profileService.getBlockList(email, page, size);
    return new ResponseEntity<>(profileBockList, HttpStatus.OK);
  }

  @Operation(summary = "Unblock user")
  @PostMapping("/unblock")
  public ResponseEntity<TextResponseModel> unBlockUser(@RequestParam("userTag") String userTag,
      Authentication authentication) {
    String email = authentication.getName();
    profileService.unBlockUser(email, userTag);
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("User unblocked", true),
        HttpStatus.OK);
  }

  @Operation(summary = "Change profile name")
  @PatchMapping("/name")
  public ResponseEntity<ProfileModel> changeProfileEmail(
      @RequestParam("name") String profileName, Authentication authentication)
      throws ProfileNotExistException, ProfileNameIsTooLongException {
    String email = authentication.getName();
    Profile profile = changeProfileInfoService.changeProfileName(email, profileName);
    return new ResponseEntity<>(ProfileModel.toModel(profile), HttpStatus.OK);
  }

  @Operation(summary = "Change profile phone")
  @PatchMapping("/phone")
  public ResponseEntity<ProfileModel> changeProfilePhone(
      @RequestParam("phoneNumber") String profilePhone, Authentication authentication)
      throws ProfileNotExistException, PhoneNumberAlreadyExistException, IncorrectPhoneNumberException {
    String email = authentication.getName();
    Profile profile = changeProfileInfoService.changeProfilePhoneNumber(email,
        profilePhone);
    return new ResponseEntity<>(ProfileModel.toModel(profile), HttpStatus.OK);
  }

  @Operation(summary = "Change profile tag")
  @PatchMapping("/tag")
  public ResponseEntity<ProfileModel> changeProfileTag(@RequestParam("tag") String profileTag,
      Authentication authentication)
      throws ProfileNotExistException, TagAlreadyExistException {
    String email = authentication.getName();
    Profile profile = changeProfileInfoService.changeProfileTag(email, profileTag);
    return new ResponseEntity<>(ProfileModel.toModel(profile), HttpStatus.OK);
  }

  @Operation(summary = "Change profile status")
  @PatchMapping("/status")
  public ResponseEntity<ProfileModel> changeProfileStatus(
      @RequestParam("status") String profileStatus, Authentication authentication)
      throws ProfileNotExistException, StatusIsTooLongException, EmptyParameterException {
    String email = authentication.getName();
    Profile profile = changeProfileInfoService.changeProfileStatus(email,
        profileStatus);
    return new ResponseEntity<>(ProfileModel.toModel(profile), HttpStatus.OK);
  }

  @Operation(summary = "Change profile picture")
  @PatchMapping("/profile-picture")
  public ResponseEntity<ProfileModel> changeProfilePicture(
      @RequestParam("file") MultipartFile profilePicture, Authentication authentication)
      throws ProfileNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    Profile profile = changeProfileInfoService.changeProfilePicture(email, profilePicture);
    return new ResponseEntity<>(ProfileModel.toModel(profile), HttpStatus.OK);
  }
}
