package com.prism.messenger.model.chat;

import com.prism.messenger.entity.Chat;
import com.prism.messenger.model.profile.FullProfileInfoModel;
import com.prism.messenger.model.profile.ProfileModel;
import java.util.Optional;
import lombok.Data;

@Data
public class ChatModel {

  private String chatId;
  private ProfileModel conversationProfile;

  public ChatModel(Optional<Chat> savedChat, FullProfileInfoModel interlocutorProfile) {
    boolean isChatExist = savedChat.isPresent();
    if (isChatExist) {
      this.chatId = savedChat.get().getId();
      this.conversationProfile = ProfileModel.toModel(interlocutorProfile);
    }
  }
}
