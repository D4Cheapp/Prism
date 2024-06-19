package com.messenger.prism.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class RequestAttemptModel {
    private Integer loginAttempt;
    private Integer resetPasswordAttempt;
    private Integer setNewEmailAttempt;
    private Date lastAttemptTime;
}
