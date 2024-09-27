package com.prism.messenger.controller;

import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.exception.relation.AddCurrentProfileToCurrentProfileException;
import com.prism.messenger.model.common.PaginationListModel;
import com.prism.messenger.model.common.TextResponseModel;
import com.prism.messenger.model.profile.ProfileTagModel;
import com.prism.messenger.model.profile.ReceiveProfileListModel;
import com.prism.messenger.service.relation.impl.RelationServiceImpl;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Relation")
@RestController
@RequestMapping("/relation")
public class RelationController {

  @Autowired
  private RelationServiceImpl relationService;

  @Operation(summary = "Add friend")
  @PostMapping("/friend")
  public ResponseEntity<TextResponseModel> addFriend(@RequestBody ProfileTagModel friendTag,
      Authentication authentication)
      throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException {
    String email = authentication.getName();
    relationService.addFriend(email, friendTag.getTag());
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Friend was added", true),
        HttpStatus.OK);
  }

  @Operation(summary = "Delete friend")
  @DeleteMapping("/friend")
  public ResponseEntity<TextResponseModel> deleteFriend(@RequestBody ProfileTagModel friendTag,
      Authentication authentication) {
    String email = authentication.getName();
    relationService.deleteFriend(email, friendTag.getTag());
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Friend deleted", true),
        HttpStatus.OK);
  }

  @Operation(summary = "Get friend profile list ")
  @GetMapping("/friend-list")
  public ResponseEntity<ReceiveProfileListModel> getFriendList(
      @RequestBody PaginationListModel requestPaginationListModel, Authentication authentication)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
      NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
      InternalException {
    String email = authentication.getName();
    Integer page = requestPaginationListModel.getPage();
    Integer size = requestPaginationListModel.getSize();
    ReceiveProfileListModel currentProfile = relationService.getFriendList(email, page, size);
    return new ResponseEntity<>(currentProfile, HttpStatus.OK);
  }

  @Operation(summary = "Get sent friend request list ")
  @GetMapping("/sent-friend-requests")
  public ResponseEntity<ReceiveProfileListModel> getSentFriendRequestList(
      @RequestBody PaginationListModel requestPaginationListModel, Authentication authentication)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
      NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
      InternalException {
    String email = authentication.getName();
    Integer page = requestPaginationListModel.getPage();
    Integer size = requestPaginationListModel.getSize();
    ReceiveProfileListModel sentRequests = relationService.getSentFriendRequestList(email, page,
        size);
    return new ResponseEntity<>(sentRequests, HttpStatus.OK);
  }

  @Operation(summary = "Decline friend request")
  @PostMapping("/friend-decline")
  public ResponseEntity<TextResponseModel> friendRequestDecline(
      @RequestBody ProfileTagModel friendTag, Authentication authentication) {
    String email = authentication.getName();
    relationService.declineFriendRequest(email, friendTag.getTag());
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Friend request was declined", true), HttpStatus.OK);
  }

  @Operation(summary = "Get friend request list ")
  @GetMapping("/friend-requests")
  public ResponseEntity<ReceiveProfileListModel> getFriendRequestList(
      @RequestBody PaginationListModel requestPaginationListModel, Authentication authentication)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
      NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
      InternalException {
    String email = authentication.getName();
    Integer page = requestPaginationListModel.getPage();
    Integer size = requestPaginationListModel.getSize();
    ReceiveProfileListModel friendRequests = relationService.getFriendRequestsList(email, page,
        size);
    return new ResponseEntity<>(friendRequests, HttpStatus.OK);
  }

  @Operation(summary = "Block user")
  @PostMapping("/block")
  public ResponseEntity<TextResponseModel> blockUser(@RequestBody ProfileTagModel userTag,
      Authentication authentication)
      throws ProfileNotExistException, AddCurrentProfileToCurrentProfileException {
    String email = authentication.getName();
    relationService.blockUser(email, userTag.getTag());
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("User blocked", true),
        HttpStatus.OK);
  }

  @Operation(summary = "Get blocked profile list ")
  @GetMapping("/block-list")
  public ResponseEntity<ReceiveProfileListModel> getBlockList(
      @RequestBody PaginationListModel requestPaginationListModel, Authentication authentication)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
      NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException,
      InternalException {
    String email = authentication.getName();
    Integer page = requestPaginationListModel.getPage();
    Integer size = requestPaginationListModel.getSize();
    ReceiveProfileListModel profileBockList = relationService.getBlockList(email, page, size);
    return new ResponseEntity<>(profileBockList, HttpStatus.OK);
  }

  @Operation(summary = "Unblock user")
  @PostMapping("/unblock")
  public ResponseEntity<TextResponseModel> unBlockUser(@RequestBody ProfileTagModel userTag,
      Authentication authentication) {
    String email = authentication.getName();
    relationService.unBlockUser(email, userTag.getTag());
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("User unblocked", true),
        HttpStatus.OK);
  }
}
