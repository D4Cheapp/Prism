package com.prism.messenger.model.profile;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import java.io.InputStream;
import java.util.Optional;
import lombok.Data;

@Data
public class ProfileModel {

  private String tag;
  private String name;
  private boolean isOnline;
  private long lastOnlineTime;
  private InputStream profilePicture;
  private String error;

  public static ProfileModel toModel(String error) {
    ProfileModel model = new ProfileModel();
    model.error = error;
    return model;
  }

  public static ProfileModel toModel(Profile profile) {
    ProfileModel model = new ProfileModel();
    model.tag = profile.getTag();
    model.name = profile.getName();
    model.isOnline = profile.isOnline();
    model.lastOnlineTime = profile.getLastOnlineTime();
    return model;
  }

  public static ProfileModel toModel(Optional<Profile> profile) throws ProfileNotExistException {
    boolean isProfileExist = profile.isPresent();
    if (isProfileExist) {
      ProfileModel model = new ProfileModel();
      Profile profileModel = profile.get();
      model.tag = profileModel.getTag();
      model.name = profileModel.getName();
      model.isOnline = profileModel.isOnline();
      model.lastOnlineTime = profileModel.getLastOnlineTime();
      return model;
    } else {
      throw new ProfileNotExistException();
    }
  }
}
