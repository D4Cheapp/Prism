package com.messenger.prism.model.auth;

import com.messenger.prism.entity.Auth;
import lombok.Data;

@Data
public class UserModel {
    private Integer id;
    private String login;
    private Boolean isDeveloper;
    private String error;

    public static UserModel toModel(Auth entity) {
        UserModel model = new UserModel();
        model.setId(entity.getId());
        model.setLogin(entity.getLogin());
        model.setIsDeveloper(entity.getRole().equals("DEVELOPER"));
        return model;
    }

    public static UserModel toModel(String error) {
        UserModel model = new UserModel();
        model.setError(error);
        return model;
    }
}
