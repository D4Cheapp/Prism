package com.messenger.prism.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Chat {
  @NonNull
  private String chatID;
  @NonNull
  private String[] messageID;
  @NonNull
  private Boolean isGroup;

  private String pinMessageID;
  private String chatPicture;

  private String firstPersonID;
  private String secondPersonID;

  private String chatName;
  private String[] memberID;
  private String adminID;
}
