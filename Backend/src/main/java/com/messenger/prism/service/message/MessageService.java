package com.messenger.prism.service.message;

import org.springframework.stereotype.Service;

import com.messenger.prism.model.Message;

@Service
public class MessageService implements MessageServiceInterface {
  @Override
  public Boolean changeIsRead(String messageID, Boolean isRead) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changeIsRead'");
  }

  @Override
  public Boolean changeMessage(String messageID, Message message) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'changeMessage'");
  }

}
