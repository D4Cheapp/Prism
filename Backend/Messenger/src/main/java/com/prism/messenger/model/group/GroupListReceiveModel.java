package com.prism.messenger.model.group;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupListReceiveModel {

  private Integer totalCount;
  private List<GroupModel> groups;
}
