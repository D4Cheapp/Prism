package com.prism.messenger.controller;

import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.group.EmptyGroupNameException;
import com.prism.messenger.model.TextResponseModel;
import com.prism.messenger.model.dialog.CreateGroupModel;
import com.prism.messenger.model.dialog.DialogIdModel;
import com.prism.messenger.model.dialog.GroupModel;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    return new ResponseEntity<>(group, HttpStatus.OK);
  }

  @DeleteMapping
  public ResponseEntity<TextResponseModel> deleteGroup(@RequestBody DialogIdModel dialogIdModel,
      Authentication authentication)
      throws PermissionsException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    groupService.deleteGroup(email, dialogIdModel.getDialogId());
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Group deleted successfully", true), HttpStatus.OK);
  }
}
