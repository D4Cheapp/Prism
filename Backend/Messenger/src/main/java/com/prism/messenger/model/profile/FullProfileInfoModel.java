package com.prism.messenger.model.profile;

import com.prism.messenger.entity.Profile;
import lombok.Data;

@Data
public class FullProfileInfoModel {

  private String tag;
  private String email;
  private String name;
  private String phoneNumber;
  private String status;
  private byte[] profilePicture;
  private boolean isOnline;
  private long lastOnlineTime;

  public FullProfileInfoModel(Profile profileModel, byte[] profilePicture) {
    this.tag = profileModel.getTag();
    this.email = profileModel.getEmail();
    this.name = profileModel.getName();
    this.phoneNumber = profileModel.getPhoneNumber();
    this.status = profileModel.getStatus();
    this.isOnline = profileModel.isOnline();
    this.lastOnlineTime = profileModel.getLastOnlineTime();
    this.profilePicture = profilePicture;
  }
}
