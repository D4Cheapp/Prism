package com.messenger.prism.service.message;

import com.messenger.prism.model.Message;

public interface MessageServiceInterface {
  Boolean changeIsRead(String messageID, Boolean isRead);

  Boolean changeMessage(String messageID, Message message);
}
