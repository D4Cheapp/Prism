package com.prism.authentication.model;

import lombok.Data;

@Data
public class EditPasswordModel {
    private Integer id;
    private String oldPassword;
    private String newPassword;
}
