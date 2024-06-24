package com.messenger.prism.model.auth;

import com.messenger.prism.entity.Auth;
import lombok.Data;

@Data
public class EmailModel {
    private Integer id;
    private String email;

    public static EmailModel toModel(Auth auth) {
        EmailModel model = new EmailModel();
        model.setId(auth.getId());
        model.setEmail(auth.getEmail());
        return model;
    }
}
