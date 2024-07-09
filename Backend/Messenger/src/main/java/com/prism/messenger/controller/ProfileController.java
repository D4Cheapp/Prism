package com.prism.messenger.controller;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.profile.AddCurrentProfileToCurrentProfileException;
import com.prism.messenger.exception.profile.IncorrectPhoneNumberException;
import com.prism.messenger.exception.profile.PhoneNumberAlreadyExistException;
import com.prism.messenger.exception.profile.ProfileNameIsTooLongException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.exception.profile.StatusIsTooLongException;
import com.prism.messenger.exception.profile.TagAlreadyExistException;
import com.prism.messenger.model.TextResponseModel;
import com.prism.messenger.model.profile.ChangeProfileNameModel;
import com.prism.messenger.model.profile.ChangeProfilePhoneModel;
import com.prism.messenger.model.profile.ChangeProfileStatusModel;
import com.prism.messenger.model.profile.FullProfileInfoModel;
import com.prism.messenger.model.profile.ProfileModel;
import com.prism.messenger.model.profile.RecieveProfileListModel;
import com.prism.messenger.model.profile.RequestProfileListModel;
import com.prism.messenger.model.profile.TagModel;
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

  @Operation(summary = "Get blocked profile list ")
  @GetMapping("/block-list")
  public ResponseEntity<RecieveProfileListModel> getBlockList(
      @RequestBody RequestProfileListModel requestProfileListModel, Authentication authentication) {
    String email = authentication.getName();
    Integer page = requestProfileListModel.getPage();
    Integer size = requestProfileListModel.getSize();
    RecieveProfileListModel profileBockList = profileService.getBlockList(email, page, size);
    return new ResponseEntity<>(profileBockList, HttpStatus.OK);
  }

  @Operation(summary = "Get friend profile list ")
  @GetMapping("/friend-list")
  public ResponseEntity<RecieveProfileListModel> getFriendList(
      @RequestBody RequestProfileListModel requestProfileListModel, Authentication authentication) {
    String email = authentication.getName();
    Integer page = requestProfileListModel.getPage();
    Integer size = requestProfileListModel.getSize();
    RecieveProfileListModel currentProfile = profileService.getFriendList(email, page, size);
    return new ResponseEntity<>(currentProfile, HttpStatus.OK);
  }

  @Operation(summary = "Get sended friend request list ")
  @GetMapping("/sended-friend-requests")
  public ResponseEntity<RecieveProfileListModel> getSendedFriendRequestList(
      @RequestBody RequestProfileListModel requestProfileListModel, Authentication authentication) {
    String email = authentication.getName();
    Integer page = requestProfileListModel.getPage();
    Integer size = requestProfileListModel.getSize();
    RecieveProfileListModel sendedRequests = profileService.getSendedFriendRequestList(email, page,
        size);
    return new ResponseEntity<>(sendedRequests, HttpStatus.OK);
  }

  @Operation(summary = "Get friend request list ")
  @GetMapping("/friend-requests")
  public ResponseEntity<RecieveProfileListModel> getFriendRequestList(
      @RequestBody RequestProfileListModel requestProfileListModel, Authentication authentication) {
    String email = authentication.getName();
    Integer page = requestProfileListModel.getPage();
    Integer size = requestProfileListModel.getSize();
    RecieveProfileListModel friendRequests = profileService.getFriendRequestsList(email, page,
        size);
    return new ResponseEntity<>(friendRequests, HttpStatus.OK);
  }

  @Operation(summary = "Add friend")
  @PostMapping("/friend")
  public ResponseEntity<TextResponseModel> addFriend(@RequestBody TagModel friendTag,
      Authentication authentication)
      throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException {
    String email = authentication.getName();
    profileService.addFriend(email, friendTag.getTag());
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Friend was added", true),
        HttpStatus.OK);
  }

  @Operation(summary = "Decline friend request")
  @PostMapping("/friend-decline")
  public ResponseEntity<TextResponseModel> friendRequestDecline(@RequestBody TagModel friendTag,
      Authentication authentication) {
    String email = authentication.getName();
    profileService.declineFriendRequest(email, friendTag.getTag());
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Friend request was declined", true), HttpStatus.OK);
  }

  @Operation(summary = "Block user")
  @PostMapping("/block")
  public ResponseEntity<TextResponseModel> blockUser(@RequestBody TagModel userTag,
      Authentication authentication)
      throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException {
    String email = authentication.getName();
    profileService.blockUser(email, userTag.getTag());
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("User blocked", true),
        HttpStatus.OK);
  }

  @Operation(summary = "Unblock user")
  @PostMapping("/unblock")
  public ResponseEntity<TextResponseModel> unBlockUser(@RequestBody TagModel friendTag,
      Authentication authentication) throws ProfileNotExistException {
    String email = authentication.getName();
    profileService.unBlockUser(email, friendTag.getTag());
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("User unblocked", true),
        HttpStatus.OK);
  }

  @Operation(summary = "Change profile name")
  @PatchMapping("/name")
  public ResponseEntity<ProfileModel> changeProfileEmail(
      @RequestBody ChangeProfileNameModel profileName, Authentication authentication)
      throws ProfileNotExistException, ProfileNameIsTooLongException {
    String email = authentication.getName();
    Profile profile = changeProfileInfoService.changeProfileName(email, profileName.getName());
    return new ResponseEntity<>(ProfileModel.toModel(profile), HttpStatus.OK);
  }

  @Operation(summary = "Change profile phone")
  @PatchMapping("/phone")
  public ResponseEntity<ProfileModel> changeProfilePhone(
      @RequestBody ChangeProfilePhoneModel profilePhone, Authentication authentication)
      throws ProfileNotExistException, PhoneNumberAlreadyExistException, IncorrectPhoneNumberException {
    String email = authentication.getName();
    Profile profile = changeProfileInfoService.changeProfilePhoneNumber(email,
        profilePhone.getPhoneNumber());
    return new ResponseEntity<>(ProfileModel.toModel(profile), HttpStatus.OK);
  }

  @Operation(summary = "Change profile tag")
  @PatchMapping("/tag")
  public ResponseEntity<ProfileModel> changeProfileTag(@RequestBody TagModel profileTag,
      Authentication authentication)
      throws ProfileNotExistException, TagAlreadyExistException, PermissionsException {
    String email = authentication.getName();
    Profile profile = changeProfileInfoService.changeProfileTag(email, profileTag.getTag());
    return new ResponseEntity<>(ProfileModel.toModel(profile), HttpStatus.OK);
  }

  @Operation(summary = "Change profile status")
  @PatchMapping("/status")
  public ResponseEntity<ProfileModel> changeProfileStatus(
      @RequestBody ChangeProfileStatusModel profileStatus, Authentication authentication)
      throws ProfileNotExistException, StatusIsTooLongException {
    String email = authentication.getName();
    Profile profile = changeProfileInfoService.changeProfileStatus(email,
        profileStatus.getStatus());
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

  @Operation(summary = "Delete friend")
  @DeleteMapping("/friend")
  public ResponseEntity<TextResponseModel> deleteFriend(@RequestBody TagModel friendTag,
      Authentication authentication)
      throws ProfileNotExistException {
    String email = authentication.getName();
    profileService.deleteFriend(email, friendTag.getTag());
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Friend deleted", true),
        HttpStatus.OK);
  }
}
