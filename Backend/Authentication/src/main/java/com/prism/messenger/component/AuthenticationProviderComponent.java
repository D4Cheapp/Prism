package com.prism.messenger.component;

import com.prism.messenger.entity.Auth;
import com.prism.messenger.repository.AuthRepo;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationProviderComponent implements AuthenticationProvider {

  @Autowired
  private AuthRepo repository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String email = authentication.getName();
    String password = authentication.getCredentials().toString();
    Auth user = repository.findByEmail(email);
    boolean isUserAlreadyExists = user == null;
    if (isUserAlreadyExists) {
      throw new BadCredentialsException("User already exists");
    }
    boolean isPasswordIncorrect = !(passwordEncoder.matches(password, user.getPassword()));
    if (isPasswordIncorrect) {
      throw new BadCredentialsException("Incorrect password");
    }
    return new UsernamePasswordAuthenticationToken(email, password, new ArrayList<>());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
