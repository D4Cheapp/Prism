package com.messenger.prism.model.auth;

import lombok.Data;

@Data
public class RestorePasswordModel {
    private String password;
    private String confirmPassword;
}
