package com.messenger.prism.service.chat;

import java.io.File;

import org.springframework.stereotype.Service;

import com.messenger.prism.model.Chat;
import com.messenger.prism.model.Message;

@Service
public class ChatService implements ChatServiceInterface {
  @Override
  public Boolean addChat(Chat chat) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addChat'");
  }

  @Override
  public Chat getChat(String chatID) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getChat'");
  }

  @Override
  public Boolean deleteChat(String chatID) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteChat'");
  }

  @Override
  public Boolean changeChatName(String chatID, String chatName) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changeChatName'");
  }

  @Override
  public Boolean changePinMessage(String charID, String messageID) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changePinMessage'");
  }

  @Override
  public Boolean deletePinMessage(String chatID) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deletePinMessage'");
  }

  @Override
  public Boolean changeChatPicture(String chatID, File chatPicture) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changeChatPicture'");
  }

  @Override
  public Boolean deleteChatPicture(String chatID) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteChatPicture'");
  }

  @Override
  public Boolean addMessage(String chatID, Message message) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addMessage'");
  }

  @Override
  public Boolean deleteMessage(String chatID, String messageID) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteMessage'");
  }

  @Override
  public Boolean addMember(String chatID, String memberID) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'addMember'");
  }

  @Override
  public Boolean deleteMember(String chatID, String memberID) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteMember'");
  }

  @Override
  public Boolean changeAdmin(String chatID, String adminID) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changeAdmin'");
  }

}
