package com.prism.messenger.model.dialog;

import java.util.List;
import lombok.Data;

@Data
public class CreateGroupModel {

  private String groupName;
  private List<String> groupMemberTags;
}
