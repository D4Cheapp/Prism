package com.messenger.prism.service.auth;

import com.messenger.prism.entity.Auth;
import com.messenger.prism.exception.PermissionsException;
import com.messenger.prism.exception.auth.*;
import com.messenger.prism.model.auth.ActivationCodeModel;
import com.messenger.prism.model.auth.UserLoginModel;
import com.messenger.prism.model.auth.UserModel;
import com.messenger.prism.model.auth.UserRegistrationModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface AuthService {
    void deleteSession(HttpServletRequest request);

    void sessionAuthentication(HttpServletRequest request, HttpServletResponse response,
                               String email, String password);

    void regitration(UserRegistrationModel user) throws IncorrectConfirmPasswordException,
            UserAlreadyExistException, EmptyPasswordException, PasswordIsTooWeakException,
            TooLongPasswordException, TooShortPasswordException, EmptyEmailException,
            IncorectEmailException;

    UserModel login(UserLoginModel user) throws UserNotFoundException, IncorrectPasswordException;

    void deleteUser(Authentication authentication, Integer id) throws UserNotFoundException,
            PermissionsException;

    UserModel getCurrentUser(Authentication authentication) throws UserNotFoundException;

    void editUserEmail(Authentication authentication, Integer id, String email) throws PermissionsException, UserNotFoundException, UserAlreadyExistException, EmptyEmailException, IncorectEmailException;

    UserModel editUserPassword(Authentication authentication, Integer id, String password) throws PermissionsException, UserNotFoundException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException;

    UserModel saveUserAfterConfirm(ActivationCodeModel user);

    void restorePasswordByEmail(String email);

    private void checkPermission(Authentication authentication, Optional<Auth> storedUser) {
    }

    private void checkPasswordValidity(String password) {
    }

    private void checkEmailValidity(String email) {

    }
}
