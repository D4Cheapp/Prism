package com.prism.messenger.model.group;

import com.prism.messenger.entity.Group;
import java.util.List;
import lombok.Data;

@Data
public class QueryGroupListReceiveModel {

  private Integer totalCount;
  private List<Group> groups;
}
