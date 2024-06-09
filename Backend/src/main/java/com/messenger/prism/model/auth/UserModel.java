package com.messenger.prism.model.auth;

import com.messenger.prism.entity.Auth;
import lombok.Data;

@Data
public class UserModel {
    private Integer id;
    private String login;
    private Boolean isAdmin;

    public static UserModel toModel(Auth entity) {
        UserModel model = new UserModel();
        model.setId(entity.getId());
        model.setLogin(entity.getLogin());
        model.setIsAdmin(entity.getRole().equals("ADMIN"));
        return model;
    }
}
