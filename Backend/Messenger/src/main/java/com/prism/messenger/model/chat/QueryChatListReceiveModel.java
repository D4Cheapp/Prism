package com.prism.messenger.model.chat;

import com.prism.messenger.entity.Chat;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryChatListReceiveModel {

  private Integer totalCount;
  private List<Chat> chats;
}
