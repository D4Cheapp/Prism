package com.prism.messenger.model.chat;

import com.prism.messenger.entity.Chat;
import com.prism.messenger.model.profile.FullProfileInfoModel;
import com.prism.messenger.model.profile.ProfileModel;
import lombok.Data;

@Data
public class ChatModel {

  private String chatId;
  private ProfileModel conversationProfile;

  public static ChatModel toModel(Chat savedChat, FullProfileInfoModel interlocutorProfile) {
    ChatModel chatModel = new ChatModel();
    chatModel.chatId = savedChat.getId();
    chatModel.conversationProfile = ProfileModel.toModel(interlocutorProfile);
    return chatModel;
  }

  public static ChatModel toModel(Chat savedChat, ProfileModel interlocutorProfile) {
    ChatModel chatModel = new ChatModel();
    chatModel.chatId = savedChat.getId();
    chatModel.conversationProfile = interlocutorProfile;
    return chatModel;
  }
}
