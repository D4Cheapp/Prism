package com.prism.messenger.service.Profile;

import com.prism.messenger.entity.Profile;

public interface ProfileService {
    void createProfile(String email);

    Profile getCurrentProfile(String email);
}
