package com.prism.messenger.model.profile;

import com.prism.messenger.entity.Profile;
import lombok.Data;

@Data
public class ProfileModel {

    private String tag;
    private String name;
    private boolean isOnline;
    private long lastOnlineTime;
    private byte[] profilePicture;
    private String error;

    public static ProfileModel toModel(Profile profile, byte[] profilePicture) {
        ProfileModel model = new ProfileModel();
        model.tag = profile.getTag();
        model.name = profile.getName();
        model.isOnline = profile.isOnline();
        model.lastOnlineTime = profile.getLastOnlineTime();
        model.profilePicture = profilePicture;
        return model;
    }

    public static ProfileModel toModel(FullProfileInfoModel profile) {
        ProfileModel model = new ProfileModel();
        model.tag = profile.getTag();
        model.name = profile.getName();
        model.isOnline = profile.isOnline();
        model.lastOnlineTime = profile.getLastOnlineTime();
        model.profilePicture = profile.getProfilePicture();
        return model;
    }
}
