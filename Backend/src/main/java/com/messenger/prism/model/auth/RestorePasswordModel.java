package com.messenger.prism.model.auth;

import lombok.Data;

@Data
public class RestorePasswordModel {
    private String oldPassword;
    private String newPassword;
}
