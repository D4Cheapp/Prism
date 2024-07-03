package com.prism.messenger.model;

import com.prism.messenger.entity.Auth;
import lombok.Data;

@Data
public class UserLoginModel {
    private String email;
    private String password;

    public static UserLoginModel toModel(Auth auth) {
        UserLoginModel user = new UserLoginModel();
        user.setEmail(auth.getEmail());
        user.setPassword(auth.getPassword());
        return user;
    }
}
