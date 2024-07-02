package com.prism.messenger.controller;

import com.prism.messenger.entity.Profile;
import com.prism.messenger.service.Profile.impl.ProfileServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Profile")
@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileServiceImpl profileService;

    @GetMapping
    public ResponseEntity<Profile> getProfile(Authentication authentication) {
        String email = authentication.getName();
        Profile currentProfile = profileService.getCurrentProfile(email);
        return new ResponseEntity<>(currentProfile, HttpStatus.OK);
    }
}
