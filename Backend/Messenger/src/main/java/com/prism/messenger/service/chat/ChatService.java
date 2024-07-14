package com.prism.messenger.service.chat;

import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.chat.ChatAlreadyExistException;
import com.prism.messenger.exception.chat.ChatCreatingException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.model.FileListModel;
import com.prism.messenger.model.chat.ChatListReceiveModel;
import com.prism.messenger.model.chat.ChatModel;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface ChatService {

  ChatModel createChat(String email, String conversationProfileTag)
      throws ProfileNotExistException, ChatAlreadyExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, ChatCreatingException;

  void deleteChat(String email, String dialogId)
      throws PermissionsException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

  ChatListReceiveModel getChatList(String email, Integer page, Integer size)
      throws ProfileNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

  FileListModel getChatFiles(String email, String dialogId, Integer page, Integer size)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, PermissionsException;
}
