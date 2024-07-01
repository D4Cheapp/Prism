package com.prism.authentication.service.auth.impl.auth;

import com.prism.authentication.entity.Auth;
import com.prism.authentication.exception.PermissionsException;
import com.prism.authentication.exception.ActivationCodeExpireException;
import com.prism.authentication.exception.UserAlreadyExistException;
import com.prism.authentication.exception.UserNotFoundException;
import com.prism.authentication.exception.email.EmptyEmailException;
import com.prism.authentication.exception.email.IncorectEmailException;
import com.prism.authentication.exception.password.*;
import com.prism.authentication.model.ActivationCodeModel;
import com.prism.authentication.model.EmailModel;
import com.prism.authentication.model.UserRegistrationModel;
import jakarta.mail.internet.AddressException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface EmailSenderService {
    void saveActivationCode(Auth account, String message) throws AddressException, IncorectEmailException;

    ActivationCodeModel getUserByEmail(String email) throws NoSuchFieldException, ActivationCodeExpireException;

    void sendRegitrationCode(UserRegistrationModel user, HttpServletRequest request) throws IncorrectConfirmPasswordException, UserAlreadyExistException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, EmptyEmailException, IncorectEmailException, AddressException;


    void sendEditUserEmailCode(EmailModel email, Authentication authentication, HttpServletRequest request) throws PermissionsException, UserNotFoundException, UserAlreadyExistException, EmptyEmailException, IncorectEmailException, AddressException, ActivationCodeExpireException;


    void sendRestorePasswordCode(String email, HttpServletRequest request) throws UserNotFoundException, AddressException, IncorectEmailException;
}
