package com.messenger.prism.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Auth {
  private String login;
  private String password;
  private Boolean isAdmin;
}
