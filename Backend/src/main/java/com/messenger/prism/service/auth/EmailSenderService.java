package com.messenger.prism.service.auth;

import com.messenger.prism.entity.Auth;
import com.messenger.prism.exception.PermissionsException;
import com.messenger.prism.exception.auth.ActivationCodeExpireException;
import com.messenger.prism.exception.auth.UserAlreadyExistException;
import com.messenger.prism.exception.auth.UserNotFoundException;
import com.messenger.prism.exception.auth.email.EmptyEmailException;
import com.messenger.prism.exception.auth.email.IncorectEmailException;
import com.messenger.prism.exception.auth.password.*;
import com.messenger.prism.model.auth.ActivationCodeModel;
import com.messenger.prism.model.auth.EmailModel;
import com.messenger.prism.model.auth.UserRegistrationModel;
import jakarta.mail.internet.AddressException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface EmailSenderService {
    void saveActivationCode(Auth account, String message) throws AddressException, IncorectEmailException;

    ActivationCodeModel getUserByEmail(String email) throws NoSuchFieldException,
            ActivationCodeExpireException;

    void sendRegitrationCode(UserRegistrationModel user, HttpServletRequest request) throws IncorrectConfirmPasswordException, UserAlreadyExistException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, EmptyEmailException, IncorectEmailException, AddressException;


    void sendEditUserEmailCode(EmailModel email, Authentication authentication,
                               HttpServletRequest request) throws PermissionsException,
            UserNotFoundException, UserAlreadyExistException, EmptyEmailException,
            IncorectEmailException, AddressException, ActivationCodeExpireException;


    void sendRestorePasswordCode(String email, HttpServletRequest request) throws UserNotFoundException, AddressException, IncorectEmailException;
}
