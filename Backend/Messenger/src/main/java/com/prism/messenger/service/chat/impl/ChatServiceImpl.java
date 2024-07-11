package com.prism.messenger.service.chat.impl;

import com.prism.messenger.entity.Chat;
import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.chat.ChatAlreadyExistException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.model.dialog.ChatModel;
import com.prism.messenger.model.profile.FullProfileInfoModel;
import com.prism.messenger.repository.ChatRepository;
import com.prism.messenger.service.chat.ChatService;
import com.prism.messenger.service.minio.impl.MinioServiceImpl;
import com.prism.messenger.service.profile.impl.ProfileServiceImpl;
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
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

  @Autowired
  private ChatRepository chatRepository;
  @Autowired
  private ProfileServiceImpl profileService;
  @Autowired
  private MinioServiceImpl minioService;

  public ChatModel createChat(String email, String interlocutorTag)
      throws ProfileNotExistException, ChatAlreadyExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    Optional<Boolean> existedChat = chatRepository.checkIsChatExist(email, interlocutorTag);
    boolean isChatExist = existedChat.isPresent() && existedChat.get();
    if (isChatExist) {
      throw new ChatAlreadyExistException();
    }
    profileService.getCurrentProfile(email);
    FullProfileInfoModel interlocutorProfile = profileService.getProfileByTag(interlocutorTag,
        email);
    String uniqueChatId = DialogUtils.generateDialogId(chatRepository::checkIsIdUnique);
    Optional<Chat> savedChat = chatRepository.createChat(uniqueChatId, email, interlocutorTag);
    minioService.createFolder("chats/" + uniqueChatId);
    return new ChatModel(savedChat, interlocutorProfile);
  }

  public void deleteChat(String email, String dialogId)
      throws PermissionsException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    Optional<Boolean> isUserInChat = chatRepository.isUserInChat(email, dialogId);
    boolean isUserHavePermissions = isUserInChat.isPresent() && isUserInChat.get();
    if (!isUserHavePermissions) {
      throw new PermissionsException("only user in chat can delete chat");
    }
    minioService.deleteFolder("chats/" + dialogId);
    chatRepository.deleteChat(dialogId);
  }
}
