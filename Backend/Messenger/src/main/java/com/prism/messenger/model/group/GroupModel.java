package com.prism.messenger.model.group;

import com.prism.messenger.entity.Group;
import com.prism.messenger.exception.group.EmptyGroupNameException;
import lombok.Data;

@Data
public class GroupModel {

  private String groupId;
  private String groupName;
  private byte[] groupImage;

  public static GroupModel toModel(CreateGroupModel createGroupModel, String uniqueGroupId)
      throws EmptyGroupNameException {
    GroupModel groupModel = new GroupModel();
    boolean isNameNotExist = createGroupModel.getGroupName() == null;
    if (isNameNotExist) {
      throw new EmptyGroupNameException();
    }
    groupModel.setGroupId(uniqueGroupId);
    groupModel.setGroupName(createGroupModel.getGroupName());
    return groupModel;
  }

  public static GroupModel toModel(Group group, byte[] groupImage) {
    GroupModel groupModel = new GroupModel();
    groupModel.setGroupId(group.getId());
    groupModel.setGroupName(group.getName());
    boolean isPhotoExist = groupImage != null;
    if (isPhotoExist) {
      groupModel.setGroupImage(groupImage);
    }
    return groupModel;
  }
}
