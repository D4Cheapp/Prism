package com.prism.messenger.service.auth.impl.auth;

import com.prism.messenger.exception.ActivationCodeExpireException;
import com.prism.messenger.exception.IncorrectConfirmCodeException;
import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.TooManyAttemptsException;
import com.prism.messenger.exception.UserNotFoundException;
import com.prism.messenger.exception.password.EmptyPasswordException;
import com.prism.messenger.exception.password.IncorrectConfirmPasswordException;
import com.prism.messenger.exception.password.IncorrectPasswordException;
import com.prism.messenger.exception.password.PasswordIsTooWeakException;
import com.prism.messenger.exception.password.TooLongPasswordException;
import com.prism.messenger.exception.password.TooShortPasswordException;
import com.prism.messenger.model.ActivationCodeModel;
import com.prism.messenger.model.EditPasswordModel;
import com.prism.messenger.model.RestorePasswordModel;
import com.prism.messenger.model.UserLoginModel;
import com.prism.messenger.model.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthService {

  void deactivateSession(HttpServletRequest request);

  void sessionAuthentication(HttpServletRequest request, String email, String password);

  UserModel login(UserLoginModel user) throws UserNotFoundException, IncorrectPasswordException;

  void deleteUser(HttpServletRequest request, Authentication authentication, Integer id)
      throws UserNotFoundException, PermissionsException;

  UserModel getCurrentUser(Authentication authentication) throws UserNotFoundException;


  UserModel editUserPassword(Authentication authentication, EditPasswordModel passwords)
      throws PermissionsException, UserNotFoundException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, IncorrectPasswordException;

  UserModel restoreUserPassword(String email, RestorePasswordModel password)
      throws ActivationCodeExpireException, IncorrectConfirmPasswordException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, IncorrectConfirmCodeException;

  UserModel saveUserAfterConfirm(ActivationCodeModel user, String code)
      throws IncorrectConfirmCodeException;

  void checkTrottleRequest(HttpServletRequest request, String type) throws TooManyAttemptsException;
}
