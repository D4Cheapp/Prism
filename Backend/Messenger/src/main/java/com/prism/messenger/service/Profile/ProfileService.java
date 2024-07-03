package com.prism.messenger.service.profile;

import com.prism.messenger.entity.Profile;

import java.util.Optional;

public interface ProfileService {
    void createProfile(String email);

    Optional<Profile> getCurrentProfile(String email);
}
