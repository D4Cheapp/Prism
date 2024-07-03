package com.prism.messenger.service.auth.impl.auth;

import com.prism.messenger.exception.*;
import com.prism.messenger.exception.password.*;
import com.prism.messenger.model.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthService {
    void deactivateSession(HttpServletRequest request);

    void sessionAuthentication(HttpServletRequest request, String email, String password);

    UserModel login(UserLoginModel user) throws UserNotFoundException, IncorrectPasswordException;

    void deleteUser(HttpServletRequest request, Authentication authentication, Integer id) throws UserNotFoundException, PermissionsException;

    UserModel getCurrentUser(Authentication authentication) throws UserNotFoundException;


    UserModel editUserPassword(Authentication authentication, EditPasswordModel passwords) throws PermissionsException, UserNotFoundException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, IncorrectPasswordException;

    UserModel restoreUserPassword(String email, RestorePasswordModel password) throws ActivationCodeExpireException, IncorrectConfirmPasswordException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, IncorrectConfirmCodeException;

    UserModel saveUserAfterConfirm(ActivationCodeModel user, String code) throws IncorrectConfirmCodeException;

    void checkTrottleRequest(HttpServletRequest request, String type) throws TooManyAttemptsException;
}
