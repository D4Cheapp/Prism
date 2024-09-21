package com.prism.messenger.service.group;

import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.group.DeleteLastAdminException;
import com.prism.messenger.exception.group.EmptyGroupNameException;
import com.prism.messenger.exception.group.GroupNotExistException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.model.common.FileListModel;
import com.prism.messenger.model.group.CreateGroupModel;
import com.prism.messenger.model.group.GroupListReceiveModel;
import com.prism.messenger.model.group.GroupModel;
import com.prism.messenger.model.profile.ProfileModel;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface GroupService {

    GroupModel createGroup(String email, CreateGroupModel createGroupModel)
            throws IOException, EmptyGroupNameException, ServerException, InsufficientDataException,
            ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException;

    void deleteGroup(String email, String groupId)
            throws PermissionsException, ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException;

    ProfileModel addGroupAdmin(String email, String profileTag, String dialogId)
            throws PermissionsException, ProfileNotExistException, ServerException,
            InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException;

    void deleteGroupAdmin(String email, String profileTag, String dialogId)
            throws PermissionsException, ProfileNotExistException, DeleteLastAdminException;

    GroupModel changeGroupName(String email, String groupId, String groupName)
            throws PermissionsException, GroupNotExistException, ServerException,
            InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException;

    GroupModel changeGroupDescription(String email, String groupId, String groupDescription)
            throws PermissionsException, GroupNotExistException, ServerException,
            InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException;

    GroupModel changeGroupPhoto(String email, String groupId, MultipartFile groupPhoto)
            throws PermissionsException, GroupNotExistException, ServerException,
            InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException;

    void addUserToGroup(String email, String memberTag, String groupId) throws PermissionsException;

    void deleteUserFromGroup(String email, String profileTag, String groupId)
            throws PermissionsException;

    GroupListReceiveModel getGroupList(String email, Integer page, Integer size)
            throws ServerException, InsufficientDataException, ErrorResponseException, IOException,
            NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException,
            XmlParserException, InternalException;

    FileListModel getGroupFiles(String email, String groupId, Integer page, Integer size)
            throws PermissionsException, ServerException, InsufficientDataException,
            ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException,
            InvalidResponseException, XmlParserException, InternalException;
}
