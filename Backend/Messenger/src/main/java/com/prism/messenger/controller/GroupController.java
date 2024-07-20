package com.prism.messenger.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.group.DeleteLastAdminException;
import com.prism.messenger.exception.group.EmptyGroupNameException;
import com.prism.messenger.exception.group.GroupNotExistException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.model.common.DialogFilesPaginationListModel;
import com.prism.messenger.model.common.DialogIdModel;
import com.prism.messenger.model.common.FileListModel;
import com.prism.messenger.model.common.PaginationListModel;
import com.prism.messenger.model.common.TextResponseModel;
import com.prism.messenger.model.group.ChangeGroupDescriptionModel;
import com.prism.messenger.model.group.ChangeGroupNameModel;
import com.prism.messenger.model.group.ChangeGroupPhotoModel;
import com.prism.messenger.model.group.CreateGroupModel;
import com.prism.messenger.model.group.GroupListReceiveModel;
import com.prism.messenger.model.group.GroupModel;
import com.prism.messenger.model.group.GroupProfileModel;
import com.prism.messenger.model.profile.ProfileModel;
import com.prism.messenger.service.group.impl.GroupServiceImpl;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Group")
@RestController
@RequestMapping("/group")
public class GroupController {

  @Autowired
  private GroupServiceImpl groupService;

  @Operation(summary = "Create group")
  @PostMapping
  public ResponseEntity<GroupModel> createGroup(@RequestBody CreateGroupModel createGroupModel,
      Authentication authentication)
      throws IOException, EmptyGroupNameException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    GroupModel group = groupService.createGroup(email, createGroupModel);
    return new ResponseEntity<>(group, CREATED);
  }

  @Operation(summary = "Delete group")
  @DeleteMapping
  public ResponseEntity<TextResponseModel> deleteGroup(@RequestBody DialogIdModel groupId,
      Authentication authentication)
      throws PermissionsException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    groupService.deleteGroup(email, groupId.getDialogId());
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Group deleted successfully", true), OK);
  }

  @Operation(summary = "Get group list with pagination")
  @GetMapping("/list")
  public ResponseEntity<GroupListReceiveModel> getGroupList(
      @RequestBody PaginationListModel groupListModel, Authentication authentication)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    Integer page = groupListModel.getPage();
    Integer size = groupListModel.getSize();
    GroupListReceiveModel group = groupService.getGroupList(email, page, size);
    return new ResponseEntity<>(group, OK);
  }

  @Operation(summary = "Add group member")
  @PostMapping("/member")
  public ResponseEntity<TextResponseModel> addMemberToGroup(
      @RequestBody GroupProfileModel groupModel, Authentication authentication)
      throws PermissionsException {
    String email = authentication.getName();
    groupService.addUserToGroup(email, groupModel.getProfileTag(), groupModel.getGroupId());
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Group member added successfully", true), OK);
  }

  @Operation(summary = "Delete group member")
  @DeleteMapping("/member")
  public ResponseEntity<TextResponseModel> deleteMemberFromGroup(
      @RequestBody GroupProfileModel groupModel, Authentication authentication)
      throws PermissionsException {
    String email = authentication.getName();
    groupService.deleteUserFromGroup(email, groupModel.getProfileTag(), groupModel.getGroupId());
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Group member deleted successfully", true), OK);
  }

  @Operation(summary = "Add group admin")
  @PostMapping("/admin")
  public ResponseEntity<ProfileModel> addGroupAdmin(@RequestBody GroupProfileModel dialogIdModel,
      Authentication authentication) throws PermissionsException, ProfileNotExistException {
    String email = authentication.getName();
    Profile admin = groupService.addGroupAdmin(email, dialogIdModel.getProfileTag(),
        dialogIdModel.getGroupId());
    return new ResponseEntity<>(ProfileModel.toModel(admin), OK);
  }

  @Operation(summary = "Delete group admin")
  @DeleteMapping("/admin")
  public ResponseEntity<TextResponseModel> deleteGroupAdmin(
      @RequestBody GroupProfileModel dialogIdModel, Authentication authentication)
      throws PermissionsException, ProfileNotExistException, DeleteLastAdminException {
    String email = authentication.getName();
    groupService.deleteGroupAdmin(email, dialogIdModel.getProfileTag(), dialogIdModel.getGroupId());
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Group admin deleted successfully", true), OK);
  }

  @Operation(summary = "Add / change group name")
  @PatchMapping("/name")
  public ResponseEntity<GroupModel> editGroupName(@RequestBody ChangeGroupNameModel groupModel,
      Authentication authentication)
      throws PermissionsException, GroupNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    GroupModel group = groupService.changeGroupName(email, groupModel.getGroupId(),
        groupModel.getGroupName());
    return new ResponseEntity<>(group, OK);
  }

  @Operation(summary = "Add / change group photo")
  @PatchMapping("/photo")
  public ResponseEntity<GroupModel> editGroupPhoto(@RequestBody ChangeGroupPhotoModel groupModel,
      Authentication authentication)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, PermissionsException, GroupNotExistException, InternalException {
    String email = authentication.getName();
    GroupModel group = groupService.changeGroupPhoto(email, groupModel.getGroupId(),
        groupModel.getGroupPhoto());
    return new ResponseEntity<>(group, OK);
  }

  @Operation(summary = "Add / change group description")
  @PatchMapping("/description")
  public ResponseEntity<GroupModel> editGroupDescription(
      @RequestBody ChangeGroupDescriptionModel groupModel, Authentication authentication)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, PermissionsException, GroupNotExistException, InternalException {
    String email = authentication.getName();
    GroupModel group = groupService.changeGroupDescription(email, groupModel.getGroupId(),
        groupModel.getGroupDescription());
    return new ResponseEntity<>(group, OK);
  }

  @Operation(summary = "Get all files of the group with pagination")
  @GetMapping("/files")
  public ResponseEntity<FileListModel> getGroupFiles(
      @RequestBody DialogFilesPaginationListModel paginationListModel,
      Authentication authentication)
      throws PermissionsException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    String dialogId = paginationListModel.getDialogId();
    Integer page = paginationListModel.getPage();
    Integer size = paginationListModel.getSize();
    FileListModel files = groupService.getGroupFiles(email, dialogId, page, size);
    return new ResponseEntity<>(files, OK);
  }
}
