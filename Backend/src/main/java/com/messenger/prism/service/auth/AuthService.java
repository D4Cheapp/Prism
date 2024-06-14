package com.messenger.prism.service.auth;

import com.messenger.prism.entity.Auth;
import com.messenger.prism.exception.PermissionsException;
import com.messenger.prism.exception.auth.ActivationCodeExpireException;
import com.messenger.prism.exception.auth.UserAlreadyExistException;
import com.messenger.prism.exception.auth.UserNotFoundException;
import com.messenger.prism.exception.auth.email.EmptyEmailException;
import com.messenger.prism.exception.auth.email.EmptyPasswordException;
import com.messenger.prism.exception.auth.email.IncorectEmailException;
import com.messenger.prism.exception.auth.password.*;
import com.messenger.prism.model.auth.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface AuthService {
    void deleteSession(HttpServletRequest request);

    void sessionAuthentication(HttpServletRequest request, HttpServletResponse response,
                               String email, String password);

    UserModel login(UserLoginModel user) throws UserNotFoundException, IncorrectPasswordException;

    void deleteUser(Authentication authentication, Integer id) throws UserNotFoundException,
            PermissionsException;

    UserModel getCurrentUser(Authentication authentication) throws UserNotFoundException;


    UserModel editUserPassword(Authentication authentication, Integer id,
                               EditPasswordModel password) throws PermissionsException,
            UserNotFoundException, EmptyPasswordException, PasswordIsTooWeakException,
            TooLongPasswordException, TooShortPasswordException, IncorrectPasswordException;

    UserModel restoreUserPassword(String code, ActivationCodeModel activationCode,
                                  RestorePasswordModel password) throws ActivationCodeExpireException, IncorrectConfirmPasswordException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException;

    UserModel saveUserAfterConfirm(ActivationCodeModel user);

    void sendRegitrationCode(UserRegistrationModel user) throws IncorrectConfirmPasswordException
            , UserAlreadyExistException, EmptyPasswordException, PasswordIsTooWeakException,
            TooLongPasswordException, TooShortPasswordException, EmptyEmailException,
            IncorectEmailException;


    void sendEditUserEmailCode(Authentication authentication, Integer id, String email) throws PermissionsException, UserNotFoundException, UserAlreadyExistException, EmptyEmailException, IncorectEmailException;


    void sendRestorePasswordCode(String email) throws UserNotFoundException;

    private void isPasswordTooWeak(String password) {

    }

    private void checkPermission(Authentication authentication, Optional<Auth> storedUser) {
    }

    private void checkPasswordValidity(String password) {
    }

    private void checkEmailValidity(String email) {

    }
}
