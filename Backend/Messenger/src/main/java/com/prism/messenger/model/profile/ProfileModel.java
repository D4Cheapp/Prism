package com.prism.messenger.model.profile;

import com.prism.messenger.entity.Profile;
import java.io.InputStream;
import lombok.Data;

@Data
public class ProfileModel {

  private String tag;
  private String name;
  private boolean isOnline;
  private long lastOnlineTime;
  private InputStream profilePicture;
  private String error;

  public static ProfileModel toModel(Profile profile) {
    ProfileModel model = new ProfileModel();
    model.tag = profile.getTag();
    model.name = profile.getName();
    model.isOnline = profile.isOnline();
    model.lastOnlineTime = profile.getLastOnlineTime();
    return model;
  }

  public static ProfileModel toModel(FullProfileInfoModel profile) {
    ProfileModel model = new ProfileModel();
    model.tag = profile.getTag();
    model.name = profile.getName();
    model.isOnline = profile.isOnline();
    model.lastOnlineTime = profile.getLastOnlineTime();
    return model;
  }
}
