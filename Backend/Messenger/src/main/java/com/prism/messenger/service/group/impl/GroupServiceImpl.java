package com.prism.messenger.service.group.impl;

import com.prism.messenger.entity.Group;
import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.group.DeleteLastAdminException;
import com.prism.messenger.exception.group.EmptyGroupNameException;
import com.prism.messenger.exception.group.GroupNotExistException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.model.FileListModel;
import com.prism.messenger.model.group.CreateGroupModel;
import com.prism.messenger.model.group.GroupListReceiveModel;
import com.prism.messenger.model.group.GroupModel;
import com.prism.messenger.model.group.QueryGroupListReceiveModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GroupServiceImpl implements GroupService {

  @Autowired
  private GroupRepository groupRepository;
  @Autowired
  private MinioServiceImpl minioService;

  public GroupModel createGroup(String email, CreateGroupModel createGroupModel)
      throws EmptyGroupNameException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, IOException {
    String uniqueGroupId = DialogUtils.generateDialogId(groupRepository::isGroupExist);
    groupRepository.createGroup(uniqueGroupId, createGroupModel.getGroupName(), email);
    minioService.createFolder("groups/" + uniqueGroupId);
    for (String memberTag : createGroupModel.getGroupMemberTags()) {
      groupRepository.addUserToGroup(memberTag, uniqueGroupId);
    }
    groupRepository.addGroupAdminByEmail(email, uniqueGroupId);
    return GroupModel.toModel(createGroupModel, uniqueGroupId);
  }

  public void deleteGroup(String email, String groupId)
      throws PermissionsException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    checkAdminPermissions(email, groupId);
    minioService.deleteFolder("groups/" + groupId);
    groupRepository.deleteGroup(groupId);
  }

  public Profile addGroupAdmin(String email, String profileTag, String dialogId)
      throws PermissionsException, ProfileNotExistException {
    checkAdminPermissions(email, dialogId);
    Optional<Profile> groupAdmin = groupRepository.addGroupAdminByTag(profileTag, dialogId);
    boolean isAdminNotExists = groupAdmin.isEmpty();
    if (isAdminNotExists) {
      throw new ProfileNotExistException();
    }
    return groupAdmin.get();
  }

  public void deleteGroupAdmin(String email, String profileTag, String dialogId)
      throws PermissionsException, ProfileNotExistException, DeleteLastAdminException {
    checkAdminPermissions(email, dialogId);
    Optional<Boolean> isGroupAdmin = groupRepository.isGroupAdminByTag(profileTag, dialogId);
    Optional<Boolean> isLastAdmin = groupRepository.isLastGroupAdmin(dialogId);
    boolean isAdminNotExists = isGroupAdmin.isEmpty() || !isGroupAdmin.get();
    boolean isLastAdminInGroup = isLastAdmin.isPresent() && isLastAdmin.get();
    if (isAdminNotExists) {
      throw new ProfileNotExistException();
    }
    if (isLastAdminInGroup) {
      throw new DeleteLastAdminException();
    }
    groupRepository.deleteGroupAdmin(profileTag, dialogId);
  }

  public GroupModel changeGroupName(String email, String groupId, String groupName)
      throws PermissionsException, GroupNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    checkAdminPermissions(email, groupId);
    Optional<Group> optionalGroup = groupRepository.changeGroupName(groupId, groupName);
    Group group = checkIsGroupExist(optionalGroup);
    byte[] photo = getGroupPhoto(group);
    return GroupModel.toModel(group, photo);
  }

  public GroupModel changeGroupDescription(String email, String groupId, String groupDescription)
      throws PermissionsException, GroupNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    checkAdminPermissions(email, groupId);
    Optional<Group> optionalGroup = groupRepository.changeGroupDescription(groupId,
        groupDescription);
    Group group = checkIsGroupExist(optionalGroup);
    byte[] photo = getGroupPhoto(group);
    return GroupModel.toModel(group, photo);
  }

  public GroupModel changeGroupPhoto(String email, String groupId, MultipartFile groupPhoto)
      throws PermissionsException, GroupNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    checkAdminPermissions(email, groupId);
    Optional<Group> optionalGroup = groupRepository.findGroupById(groupId);
    Group group = checkIsGroupExist(optionalGroup);
    String photoPath = "groups/" + groupId + "/groupPhoto.jpg";
    minioService.addFile(photoPath, groupPhoto);
    byte[] photo = groupPhoto.getBytes();
    return GroupModel.toModel(group, photo);
  }

  public void addUserToGroup(String email, String memberTag, String groupId)
      throws PermissionsException {
    checkIsUserInGroup(email, groupId);
    groupRepository.addUserToGroup(memberTag, groupId);
  }

  public void deleteUserFromGroup(String email, String profileTag, String groupId)
      throws PermissionsException {
    checkAdminPermissions(email, groupId);
    groupRepository.deleteGroupMember(groupId, profileTag);
  }

  public GroupListReceiveModel getGroupList(String email, Integer page, Integer size)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    Optional<QueryGroupListReceiveModel> groupList = groupRepository.getGroupList(email,
        page * size, size);
    boolean isTotalCountEmpty =
        groupList.isPresent() && groupList.get().getTotalCount() == 0 || groupList.isEmpty();
    boolean isGroupListEmpty = groupList.isPresent() && isTotalCountEmpty || groupList.isEmpty();
    if (isGroupListEmpty) {
      return new GroupListReceiveModel(0, null);
    }
    Integer totalCount = groupList.get().getTotalCount();
    List<GroupModel> groupModels = new ArrayList<>();
    for (Group group : groupList.get().getGroups()) {
      byte[] photo = getGroupPhoto(group);
      groupModels.add(GroupModel.toModel(group, photo));
    }
    return new GroupListReceiveModel(totalCount, groupModels);

  }

  public FileListModel getGroupFiles(String email, String groupId, Integer page, Integer size)
      throws PermissionsException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    checkIsUserInGroup(email, groupId);
    return minioService.getDialogFiles("groups/" + groupId, page, size);
  }

  private void checkAdminPermissions(String email, String dialogId) throws PermissionsException {
    Optional<Boolean> isGroupAdmin = groupRepository.isGroupAdminByEmail(email, dialogId);
    boolean isUserHavePermissions = isGroupAdmin.isPresent() && isGroupAdmin.get();
    if (!isUserHavePermissions) {
      throw new PermissionsException("only group admin can delete group");
    }
  }

  private byte[] getGroupPhoto(Group group)
      throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    boolean isPhotoExists = group.getPicturePath() != null;
    if (isPhotoExists) {
      return minioService.getFile(group.getPicturePath());
    }
    return null;
  }

  private Group checkIsGroupExist(Optional<Group> group) throws GroupNotExistException {
    boolean isGroupEmpty = group.isEmpty();
    if (isGroupEmpty) {
      throw new GroupNotExistException();
    }
    return group.get();
  }

  private void checkIsUserInGroup(String email, String groupId) throws PermissionsException {
    Optional<Boolean> isUserInGroup = groupRepository.isUserInGroup(email, groupId);
    boolean isUserHavePermissions = isUserInGroup.isPresent() && isUserInGroup.get();
    if (!isUserHavePermissions) {
      throw new PermissionsException("you are not in the group");
    }
  }
}
