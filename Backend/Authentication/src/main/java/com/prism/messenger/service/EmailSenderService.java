package com.prism.messenger.service;

import com.prism.messenger.entity.Auth;
import com.prism.messenger.exception.ActivationCodeExpireException;
import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.UserAlreadyExistException;
import com.prism.messenger.exception.UserNotFoundException;
import com.prism.messenger.exception.email.EmptyEmailException;
import com.prism.messenger.exception.email.IncorectEmailException;
import com.prism.messenger.exception.password.EmptyPasswordException;
import com.prism.messenger.exception.password.IncorrectConfirmPasswordException;
import com.prism.messenger.exception.password.PasswordIsTooWeakException;
import com.prism.messenger.exception.password.TooLongPasswordException;
import com.prism.messenger.exception.password.TooShortPasswordException;
import com.prism.messenger.model.ActivationCodeModel;
import com.prism.messenger.model.ChangeEmailModel;
import com.prism.messenger.model.UserRegistrationModel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface EmailSenderService {

  void saveActivationCode(Auth account, String message);

  ActivationCodeModel getUserByEmail(String email) throws ActivationCodeExpireException;

  void sendRegistrationCode(UserRegistrationModel user, HttpServletRequest request)
      throws IncorrectConfirmPasswordException, UserAlreadyExistException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, EmptyEmailException, IncorectEmailException;


  void sendEditUserEmailCode(ChangeEmailModel email, Authentication authentication,
      HttpServletRequest request)
      throws UserNotFoundException, PermissionsException, UserAlreadyExistException, EmptyEmailException, IncorectEmailException;


  void sendRestorePasswordCode(String email, HttpServletRequest request)
      throws UserNotFoundException;
}
