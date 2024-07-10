package com.prism.messenger.model.group;

import java.util.List;
import lombok.Data;

@Data
public class CreateGroupModel {

  private String groupName;
  private List<String> groupMemberTags;
}
