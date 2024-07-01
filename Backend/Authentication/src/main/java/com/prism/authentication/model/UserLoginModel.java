package com.prism.authentication.model;

import com.prism.authentication.entity.Auth;
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
