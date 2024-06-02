package com.messenger.prism.service;

import com.messenger.prism.config.AuthUserDetails;
import com.messenger.prism.entity.AuthEntity;
import com.messenger.prism.repository.AuthRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class AuthUserDetailsService implements UserDetailsService {
    @Autowired
    private AuthRepo repo;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<AuthEntity> user = Optional.ofNullable(repo.findByLogin(login));
        return user.map(AuthUserDetails::new).orElseThrow(() ->
                new UsernameNotFoundException("User " + login + " not found"));
    }
}
