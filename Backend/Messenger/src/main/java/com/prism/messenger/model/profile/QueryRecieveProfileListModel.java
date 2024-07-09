package com.prism.messenger.model.profile;

import com.prism.messenger.entity.Profile;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QueryRecieveProfileListModel {

  private Integer totalCount;
  private List<Profile> profiles;
}
