package com.prism.messenger.service.group;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.group.DeleteLastAdminException;
import com.prism.messenger.exception.group.EmptyGroupNameException;
import com.prism.messenger.exception.group.GroupNotExistException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.model.dialog.CreateGroupModel;
import com.prism.messenger.model.dialog.GroupModel;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.springframework.web.multipart.MultipartFile;

public interface GroupService {

  GroupModel createGroup(String email,
      CreateGroupModel createGroupModel)
      throws IOException, EmptyGroupNameException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

  void deleteGroup(String email, String groupId)
      throws PermissionsException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

  Profile addGroupAdmin(String email, String profileTag, String dialogId)
      throws PermissionsException, ProfileNotExistException;

  void deleteGroupAdmin(String email, String profileTag, String dialogId)
      throws PermissionsException, ProfileNotExistException, DeleteLastAdminException;

  GroupModel changeGroupName(String email, String groupId, String groupName)
      throws PermissionsException, GroupNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

  GroupModel changeGroupDescription(String email, String groupId, String groupDescription)
      throws PermissionsException, GroupNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

  GroupModel changeGroupPhoto(String email, String groupId, MultipartFile groupPhoto)
      throws PermissionsException, GroupNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
}
