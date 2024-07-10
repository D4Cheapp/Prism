package com.prism.messenger.service.group.impl;

import com.prism.messenger.exception.group.EmptyGroupNameException;
import com.prism.messenger.model.group.CreateGroupModel;
import com.prism.messenger.model.group.GroupModel;
import com.prism.messenger.repository.GroupRepository;
import com.prism.messenger.service.group.GroupService;
import com.prism.messenger.service.minio.impl.MinioServiceImpl;
import com.prism.messenger.util.DialogUtils;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

  @Autowired
  private GroupRepository groupRepository;
  @Autowired
  private MinioServiceImpl minioService;

  public GroupModel createGroup(String email, CreateGroupModel createGroupModel)
      throws EmptyGroupNameException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, IOException {
    String uniqueGroupId = DialogUtils.generateDialogId(groupRepository::findOneById);
    groupRepository.createGroup(uniqueGroupId, createGroupModel.getGroupName(), email);
    minioService.createFolder("groups/" + uniqueGroupId);
    for (String memberTag : createGroupModel.getGroupMemberTags()) {
      addUserToGroup(memberTag, uniqueGroupId);
    }
    return GroupModel.toModel(createGroupModel, uniqueGroupId);
  }

  private void addUserToGroup(String memberTag, String groupId) {
    groupRepository.addUserToGroup(memberTag, groupId);
  }
}
