package com.messenger.prism.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class User {
  @NonNull
  private String userID;
  @NonNull
  private String name;
  @NonNull
  private Boolean isOnline;
  @NonNull
  private String[] chatIDs = {};

  private String profilePicture;
}
