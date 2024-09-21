package com.prism.messenger.controller;

import com.prism.messenger.exception.EmptyParameterException;
import com.prism.messenger.exception.profile.*;
import com.prism.messenger.model.common.PaginationListModel;
import com.prism.messenger.model.common.TextResponseModel;
import com.prism.messenger.model.profile.*;
import com.prism.messenger.service.profile.impl.ChangeProfileInfoServiceImpl;
import com.prism.messenger.service.profile.impl.ProfileServiceImpl;
import io.minio.errors.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
        FullProfileInfoModel foundedProfiles = profileService.getProfileByTelephone(telephone,
                email);
        return new ResponseEntity<>(foundedProfiles, HttpStatus.OK);
    }

    @Operation(summary = "Search profile by tag")
    @GetMapping("/search/{tag}")
    public ResponseEntity<ReceiveProfileListModel> searchProfileByTag(@PathVariable String tag,
                                                                      @RequestBody PaginationListModel requestPaginationListModel)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        Integer page = requestPaginationListModel.getPage();
        Integer size = requestPaginationListModel.getSize();
        ReceiveProfileListModel foundedProfiles = profileService.searchProfileByTag(tag, page,
                size);
        return new ResponseEntity<>(foundedProfiles, HttpStatus.OK);
    }

    @Operation(summary = "Add friend")
    @PostMapping("/friend")
    public ResponseEntity<TextResponseModel> addFriend(@RequestBody ProfileTagModel friendTag,
                                                       Authentication authentication)
            throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException {
        String email = authentication.getName();
        profileService.addFriend(email, friendTag.getTag());
        return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Friend was added", true),
                HttpStatus.OK);
    }

    @Operation(summary = "Delete friend")
    @DeleteMapping("/friend")
    public ResponseEntity<TextResponseModel> deleteFriend(@RequestBody ProfileTagModel friendTag,
                                                          Authentication authentication) {
        String email = authentication.getName();
        profileService.deleteFriend(email, friendTag.getTag());
        return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Friend deleted", true),
                HttpStatus.OK);
    }

    @Operation(summary = "Get friend profile list ")
    @GetMapping("/friend-list")
    public ResponseEntity<ReceiveProfileListModel> getFriendList(
            @RequestBody PaginationListModel requestPaginationListModel,
            Authentication authentication)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        String email = authentication.getName();
        Integer page = requestPaginationListModel.getPage();
        Integer size = requestPaginationListModel.getSize();
        ReceiveProfileListModel currentProfile = profileService.getFriendList(email, page, size);
        return new ResponseEntity<>(currentProfile, HttpStatus.OK);
    }

    @Operation(summary = "Get sent friend request list ")
    @GetMapping("/sent-friend-requests")
    public ResponseEntity<ReceiveProfileListModel> getSentFriendRequestList(
            @RequestBody PaginationListModel requestPaginationListModel,
            Authentication authentication)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        String email = authentication.getName();
        Integer page = requestPaginationListModel.getPage();
        Integer size = requestPaginationListModel.getSize();
        ReceiveProfileListModel sentRequests = profileService.getSentFriendRequestList(email, page,
                size);
        return new ResponseEntity<>(sentRequests, HttpStatus.OK);
    }

    @Operation(summary = "Decline friend request")
    @PostMapping("/friend-decline")
    public ResponseEntity<TextResponseModel> friendRequestDecline(
            @RequestBody ProfileTagModel friendTag,
            Authentication authentication) {
        String email = authentication.getName();
        profileService.declineFriendRequest(email, friendTag.getTag());
        return new ResponseEntity<>(
                TextResponseModel.toTextResponseModel("Friend request was declined", true),
                HttpStatus.OK);
    }

    @Operation(summary = "Get friend request list ")
    @GetMapping("/friend-requests")
    public ResponseEntity<ReceiveProfileListModel> getFriendRequestList(
            @RequestBody PaginationListModel requestPaginationListModel,
            Authentication authentication)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        String email = authentication.getName();
        Integer page = requestPaginationListModel.getPage();
        Integer size = requestPaginationListModel.getSize();
        ReceiveProfileListModel friendRequests = profileService.getFriendRequestsList(email, page,
                size);
        return new ResponseEntity<>(friendRequests, HttpStatus.OK);
    }

    @Operation(summary = "Block user")
    @PostMapping("/block")
    public ResponseEntity<TextResponseModel> blockUser(@RequestBody ProfileTagModel userTag,
                                                       Authentication authentication)
            throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException {
        String email = authentication.getName();
        profileService.blockUser(email, userTag.getTag());
        return new ResponseEntity<>(TextResponseModel.toTextResponseModel("User blocked", true),
                HttpStatus.OK);
    }

    @Operation(summary = "Get blocked profile list ")
    @GetMapping("/block-list")
    public ResponseEntity<ReceiveProfileListModel> getBlockList(
            @RequestBody PaginationListModel requestPaginationListModel,
            Authentication authentication)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        String email = authentication.getName();
        Integer page = requestPaginationListModel.getPage();
        Integer size = requestPaginationListModel.getSize();
        ReceiveProfileListModel profileBockList = profileService.getBlockList(email, page, size);
        return new ResponseEntity<>(profileBockList, HttpStatus.OK);
    }

    @Operation(summary = "Unblock user")
    @PostMapping("/unblock")
    public ResponseEntity<TextResponseModel> unBlockUser(@RequestBody ProfileTagModel userTag,
                                                         Authentication authentication) {
        String email = authentication.getName();
        profileService.unBlockUser(email, userTag.getTag());
        return new ResponseEntity<>(TextResponseModel.toTextResponseModel("User unblocked", true),
                HttpStatus.OK);
    }

    @Operation(summary = "Change profile name")
    @PatchMapping("/name")
    public ResponseEntity<ProfileModel> changeProfileEmail(
            @RequestBody ChangeProfilePropertyModel profileName, Authentication authentication)
            throws ProfileNotExistException, ProfileNameIsTooLongException, ServerException,
            InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException,
            InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String email = authentication.getName();
        ProfileModel profile = changeProfileInfoService.changeProfileName(email,
                profileName.getProperty());
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @Operation(summary = "Change profile phone")
    @PatchMapping("/phone")
    public ResponseEntity<ProfileModel> changeProfilePhone(
            @RequestBody ChangeProfilePropertyModel profilePhone, Authentication authentication)
            throws ProfileNotExistException, PhoneNumberAlreadyExistException,
            IncorrectPhoneNumberException, ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        String email = authentication.getName();
        ProfileModel profile = changeProfileInfoService.changeProfilePhoneNumber(email,
                profilePhone.getProperty());
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @Operation(summary = "Change profile tag")
    @PatchMapping("/tag")
    public ResponseEntity<ProfileModel> changeProfileTag(@RequestBody ProfileTagModel profileTag,
                                                         Authentication authentication)
            throws ProfileNotExistException, TagAlreadyExistException, ServerException,
            InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        String email = authentication.getName();
        ProfileModel profile = changeProfileInfoService.changeProfileTag(email,
                profileTag.getTag());
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @Operation(summary = "Change profile status")
    @PatchMapping("/status")
    public ResponseEntity<ProfileModel> changeProfileStatus(
            @RequestBody ChangeProfilePropertyModel profileStatus, Authentication authentication)
            throws ProfileNotExistException, StatusIsTooLongException, EmptyParameterException,
            ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException {
        String email = authentication.getName();
        ProfileModel profile = changeProfileInfoService.changeProfileStatus(email,
                profileStatus.getProperty());
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @Operation(summary = "Change profile picture")
    @PatchMapping("/profile-picture")
    public ResponseEntity<ProfileModel> changeProfilePicture(
            @RequestParam("file") MultipartFile profilePicture, Authentication authentication)
            throws ProfileNotExistException, ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException {
        String email = authentication.getName();
        ProfileModel profile = changeProfileInfoService.changeProfilePicture(email, profilePicture);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }
}
