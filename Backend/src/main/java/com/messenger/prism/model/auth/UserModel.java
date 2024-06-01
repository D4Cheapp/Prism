package com.messenger.prism.model.auth;

import com.messenger.prism.entity.AuthEntity;
import lombok.Data;

@Data
public class UserModel {
    private Long id;
    private String login;
    private Boolean isAdmin;

    public static UserModel toModel(AuthEntity entity) {
        UserModel model = new UserModel();
        model.setId(entity.getId());
        model.setLogin(entity.getLogin());
        model.setIsAdmin(entity.getRole().equals("ADMIN"));
        return model;
    }
}
