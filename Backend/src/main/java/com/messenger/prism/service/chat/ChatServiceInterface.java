package com.messenger.prism.service.chat;

import java.io.File;

import com.messenger.prism.model.Chat;
import com.messenger.prism.model.Message;

public interface ChatServiceInterface {
  Boolean addChat(Chat chat);

  Chat getChat(String chatID);

  Boolean deleteChat(String chatID);

  Boolean changeChatName(String chatID, String chatName);

  Boolean changePinMessage(String charID, String messageID);

  Boolean deletePinMessage(String chatID);

  Boolean changeChatPicture(String chatID, File chatPicture);

  Boolean deleteChatPicture(String chatID);

  Boolean addMessage(String chatID, Message message);

  Boolean deleteMessage(String chatID, String messageID);

  Boolean addMember(String chatID, String memberID);

  Boolean deleteMember(String chatID, String memberID);

  Boolean changeAdmin(String chatID, String adminID);
}
