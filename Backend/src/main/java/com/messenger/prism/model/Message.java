package com.messenger.prism.model;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Message {
  @NonNull
  private String messageID;
  @NonNull
  private String userID;
  @NonNull
  private String text;
  @NonNull
  private Data receiveTime;
  @NonNull
  private Boolean isRead;
  @NonNull
  private Boolean isEdited;

  private String replyID;
  private String voiceMessage;
  private String videoMessage;
  private String[] file;
  private String[] photo;
}
