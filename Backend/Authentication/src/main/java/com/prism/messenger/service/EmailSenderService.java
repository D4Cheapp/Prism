package com.prism.messenger.service.auth.impl.auth;

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
import com.prism.messenger.model.EmailModel;
import com.prism.messenger.model.UserRegistrationModel;
import jakarta.mail.internet.AddressException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface EmailSenderService {

  void saveActivationCode(Auth account, String message)
      throws AddressException, IncorectEmailException;

  ActivationCodeModel getUserByEmail(String email)
      throws NoSuchFieldException, ActivationCodeExpireException;

  void sendRegitrationCode(UserRegistrationModel user, HttpServletRequest request)
      throws IncorrectConfirmPasswordException, UserAlreadyExistException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, EmptyEmailException, IncorectEmailException, AddressException;


  void sendEditUserEmailCode(EmailModel email, Authentication authentication,
      HttpServletRequest request)
      throws PermissionsException, UserNotFoundException, UserAlreadyExistException, EmptyEmailException, IncorectEmailException, AddressException, ActivationCodeExpireException;


  void sendRestorePasswordCode(String email, HttpServletRequest request)
      throws UserNotFoundException, AddressException, IncorectEmailException;
}
