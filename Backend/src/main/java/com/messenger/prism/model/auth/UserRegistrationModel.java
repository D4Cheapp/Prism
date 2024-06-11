package com.messenger.prism.model.auth;

import com.messenger.prism.entity.Auth;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
public class UserRegistrationModel {
    private String email;
    private String password;
    private String confirmPassword;
    private Boolean isDeveloper;

    public static Auth toEntity(UserRegistrationModel model,
                                PasswordEncoder encoder) {
        Auth entity = new Auth();
        String role = model.getIsDeveloper() ? "DEVELOPER" : "USER";
        entity.setRole(role);
        entity.setEmail(model.getEmail());
        entity.setPassword(encoder.encode(model.getPassword()));
        return entity;
    }
}
