package com.messenger.prism.model.auth;

import lombok.Data;

@Data
public class EditPasswordModel {
    private String oldPassword;
    private String newPassword;
}
