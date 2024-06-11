package com.messenger.prism.model.auth;

import lombok.Data;

@Data
public class UserLoginModel {
    private String email;
    private String password;
}
