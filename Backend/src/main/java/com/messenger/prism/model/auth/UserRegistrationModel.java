package com.messenger.prism.model.auth;

import com.messenger.prism.entity.AuthEntity;

public class UserRegistrationModel {
    private String login;
    private String password;
    private String confirmPassword;
    private Boolean isAdmin;

    public static AuthEntity toEntity(UserRegistrationModel model) {
        AuthEntity entity = new AuthEntity();
        entity.setLogin(model.getLogin());
        entity.setPassword(model.getPassword());
        entity.setIsAdmin(model.getIsAdmin());
        return entity;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
