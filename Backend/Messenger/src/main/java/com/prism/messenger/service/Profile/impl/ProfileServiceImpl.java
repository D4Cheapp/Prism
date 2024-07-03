package com.prism.messenger.service.profile.impl;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.repository.ProfileRepository;
import com.prism.messenger.service.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public void createProfile(String email) {
        Profile profile = new Profile();
        profile.setEmail(email);
        profile.setTag("@user" + email.hashCode());
        profile.setName("username" + email.hashCode());
        profile.setLastOnlineTime(new Date().getTime());
        profileRepository.save(profile);
    }

    public Optional<Profile> getCurrentProfile(String email) {
        return profileRepository.findByEmail(email);
    }
}
