package com.messenger.prism.model.auth;

import lombok.Data;

@Data
public class ActivationCodeModel {
    public String email;
    public String code;
}
