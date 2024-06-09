package com.messenger.prism.model.auth;

import com.messenger.prism.entity.Auth;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class UserRegistrationModel {
    private String login;
    private String password;
    private String confirmPassword;
    private Boolean isAdmin;

    public static Auth toEntity(UserRegistrationModel model, PasswordEncoder encoder) {
        Auth entity = new Auth();
        String role = model.getIsAdmin() ? "ADMIN" : "USER";
        entity.setRole(role);
        entity.setLogin(model.getLogin());
        entity.setPassword(encoder.encode(model.getPassword()));
        return entity;
    }
}
