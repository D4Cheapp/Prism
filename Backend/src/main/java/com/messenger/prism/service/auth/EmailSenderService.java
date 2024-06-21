package com.messenger.prism.service.auth;

import com.messenger.prism.entity.Auth;
import com.messenger.prism.exception.PermissionsException;
import com.messenger.prism.exception.auth.ActivationCodeExpireException;
import com.messenger.prism.exception.auth.UserAlreadyExistException;
import com.messenger.prism.exception.auth.UserNotFoundException;
import com.messenger.prism.exception.auth.email.EmptyEmailException;
import com.messenger.prism.exception.auth.password.EmptyPasswordException;
import com.messenger.prism.exception.auth.email.IncorectEmailException;
import com.messenger.prism.exception.auth.password.IncorrectConfirmPasswordException;
import com.messenger.prism.exception.auth.password.PasswordIsTooWeakException;
import com.messenger.prism.exception.auth.password.TooLongPasswordException;
import com.messenger.prism.exception.auth.password.TooShortPasswordException;
import com.messenger.prism.model.auth.ActivationCodeModel;
import com.messenger.prism.model.auth.EmailModel;
import com.messenger.prism.model.auth.UserRegistrationModel;
import org.springframework.security.core.Authentication;

public interface EmailSenderService {
    void saveActivationCode(Auth account, String message);

    ActivationCodeModel getUserByActivationCode(String code) throws NoSuchFieldException, ActivationCodeExpireException;

    void sendRegitrationCode(UserRegistrationModel user) throws IncorrectConfirmPasswordException
            , UserAlreadyExistException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, EmptyEmailException, IncorectEmailException;


    void sendEditUserEmailCode(Authentication authentication, EmailModel email) throws PermissionsException, UserNotFoundException, UserAlreadyExistException, EmptyEmailException, IncorectEmailException;


    void sendRestorePasswordCode(String email) throws UserNotFoundException;
}
