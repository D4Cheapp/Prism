package com.messenger.prism.model.auth;

import com.messenger.prism.entity.Auth;
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
