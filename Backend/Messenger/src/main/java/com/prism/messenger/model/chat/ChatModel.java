package com.prism.messenger.model.chat;

import com.prism.messenger.entity.Chat;
import com.prism.messenger.model.profile.FullProfileInfoModel;
import com.prism.messenger.model.profile.ProfileModel;
import lombok.Data;

@Data
public class ChatModel {

  private String chatId;
  private ProfileModel conversationProfile;

  public ChatModel(Chat savedChat, FullProfileInfoModel interlocutorProfile) {
    this.chatId = savedChat.getId();
    this.conversationProfile = ProfileModel.toModel(interlocutorProfile);
  }
}
