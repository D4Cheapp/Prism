package com.messenger.prism.model.auth;

import com.messenger.prism.entity.AuthEntity;

public class UserModel {
    private Long id;
    private String login;
    private Boolean isAdmin;

    public static UserModel toModel(AuthEntity entity) {
        UserModel model = new UserModel();
        model.setId(entity.getId());
        model.setLogin(entity.getLogin());
        model.setAdmin(entity.getIsAdmin());
        return model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
