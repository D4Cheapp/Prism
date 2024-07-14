package com.prism.messenger.model.chat;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatListReceiveModel {

  private Integer totalCount;
  private List<ChatModel> chats;
}
