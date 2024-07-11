package com.prism.messenger.model.dialog;

import com.prism.messenger.exception.group.EmptyGroupNameException;
import lombok.Data;

@Data
public class GroupModel {

  private String groupId;
  private String groupName;
  private byte[] groupImage;

  public static GroupModel toModel(CreateGroupModel createGroupModel, String uniqueChatId)
      throws EmptyGroupNameException {
    GroupModel groupModel = new GroupModel();
    boolean isNameNotExist = createGroupModel.getGroupName() == null;
    if (isNameNotExist) {
      throw new EmptyGroupNameException();
    }
    groupModel.setGroupId(uniqueChatId);
    groupModel.setGroupName(createGroupModel.getGroupName());
    return groupModel;
  }
}
