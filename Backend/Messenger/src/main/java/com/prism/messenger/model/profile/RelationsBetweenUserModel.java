package com.prism.messenger.model.profile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RelationsBetweenUserModel {

  private String relationToUser;
  private String relationFromUser;
}
