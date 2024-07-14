package com.prism.messenger.service.chat.impl;

import com.prism.messenger.entity.Chat;
import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.chat.ChatAlreadyExistException;
import com.prism.messenger.exception.chat.ChatCreatingException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.model.FileListModel;
import com.prism.messenger.model.chat.ChatListReceiveModel;
import com.prism.messenger.model.chat.ChatModel;
import com.prism.messenger.model.chat.QueryChatListReceiveModel;
import com.prism.messenger.model.profile.FullProfileInfoModel;
import com.prism.messenger.model.profile.ProfileModel;
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
import java.util.ArrayList;
import java.util.List;
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
      throws ProfileNotExistException, ChatAlreadyExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, ChatCreatingException {
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
    boolean isChatCreated = savedChat.isPresent();
    if (!isChatCreated) {
      throw new ChatCreatingException();
    }
    minioService.createFolder("chats/" + uniqueChatId);
    return ChatModel.toModel(savedChat.get(), interlocutorProfile);
  }

  public void deleteChat(String email, String dialogId)
      throws PermissionsException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    checkIsUserInChat(email, dialogId);
    minioService.deleteFolder("chats/" + dialogId);
    chatRepository.deleteChat(dialogId);
  }

  public ChatListReceiveModel getChatList(String email, Integer page, Integer size)
      throws ProfileNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    Optional<QueryChatListReceiveModel> chats = chatRepository.getChatList(email, page * size,
        size);
    boolean isTotalCountEmpty =
        chats.isPresent() && chats.get().getTotalCount() == 0 || chats.isEmpty();
    boolean isChatsEmpty = chats.isPresent() && isTotalCountEmpty || chats.isEmpty();
    if (isChatsEmpty) {
      return new ChatListReceiveModel(0, null);
    }
    Integer totalCount = chats.get().getTotalCount();
    List<ChatModel> chatModels = new ArrayList<>();
    for (Chat chat : chats.get().getChats()) {
      Optional<String> interlocutorProfile = chatRepository.getInterlocutorProfile(email,
          chat.getId());
      boolean isInterlocutorProfileEmpty = interlocutorProfile.isEmpty();
      if (isInterlocutorProfileEmpty) {
        throw new ProfileNotExistException();
      }
      chatModels.add(ChatModel.toModel(chat,
          ProfileModel.toModel(profileService.getProfileByTag(interlocutorProfile.get(), email))));
    }
    return new ChatListReceiveModel(totalCount, chatModels);
  }

  public FileListModel getChatFiles(String email, String dialogId, Integer page, Integer size)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, PermissionsException {
    checkIsUserInChat(email, dialogId);
    return minioService.getDialogFiles("chats/" + dialogId, page, size);
  }

  private void checkIsUserInChat(String email, String dialogId) throws PermissionsException {
    Optional<Boolean> isUserInChat = chatRepository.isUserInChat(email, dialogId);
    boolean isUserHavePermissions = isUserInChat.isPresent() && isUserInChat.get();
    if (!isUserHavePermissions) {
      throw new PermissionsException("only user in chat can delete chat");
    }
  }
}
