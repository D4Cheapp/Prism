package com.messenger.prism.service.auth;

import com.messenger.prism.exception.PermissionsException;
import com.messenger.prism.exception.TooManyAttemptsException;
import com.messenger.prism.exception.auth.ActivationCodeExpireException;
import com.messenger.prism.exception.auth.IncorrectConfirmCodeException;
import com.messenger.prism.exception.auth.UserNotFoundException;
import com.messenger.prism.exception.auth.password.*;
import com.messenger.prism.model.auth.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {
    void deactivateSession(HttpServletRequest request);

    void sessionAuthentication(HttpServletRequest request, HttpServletResponse response,
                               String email, String password);

    UserModel login(UserLoginModel user) throws UserNotFoundException, IncorrectPasswordException;

    void deleteUser(Authentication authentication, Integer id) throws UserNotFoundException,
            PermissionsException;

    UserModel getCurrentUser(Authentication authentication) throws UserNotFoundException;


    UserModel editUserPassword(Authentication authentication, EditPasswordModel passwords) throws PermissionsException, UserNotFoundException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, IncorrectPasswordException;

    UserModel restoreUserPassword(String email, RestorePasswordModel password) throws ActivationCodeExpireException, IncorrectConfirmPasswordException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, IncorrectConfirmCodeException;

    UserModel saveUserAfterConfirm(ActivationCodeModel user, String code) throws IncorrectConfirmCodeException;

    void checkTrottleRequest(HttpServletRequest request, String type) throws TooManyAttemptsException;
}
