package com.messenger.prism.service.auth;

import com.messenger.prism.entity.Auth;
import com.messenger.prism.exception.auth.ActivationCodeExpireException;
import com.messenger.prism.model.auth.ActivationCodeModel;

public interface EmailSenderService {
    void saveActivationCode(Auth account, String message, String activationUrl);

    ActivationCodeModel getUserByActivationCode(String code) throws NoSuchFieldException, ActivationCodeExpireException;
}
