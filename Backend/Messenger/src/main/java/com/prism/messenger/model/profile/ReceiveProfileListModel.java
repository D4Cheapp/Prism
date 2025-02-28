package com.prism.messenger.model.profile;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReceiveProfileListModel {

  private Integer totalCount;
  private List<ProfileModel> profiles;
}
