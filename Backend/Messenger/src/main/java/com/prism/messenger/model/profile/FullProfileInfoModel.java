package com.prism.messenger.model.profile;

import com.prism.messenger.entity.Profile;
import java.util.Optional;
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

  public FullProfileInfoModel(Optional<Profile> profile, byte[] profilePicture) {
    boolean isProfileExist = profile.isPresent();
    if (isProfileExist) {
      Profile profileModel = profile.get();
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
}
