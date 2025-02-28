package com.prism.messenger.service.impl;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

import com.prism.messenger.entity.Auth;
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
import com.prism.messenger.model.RequestAttemptModel;
import com.prism.messenger.model.RestorePasswordModel;
import com.prism.messenger.model.UserLoginModel;
import com.prism.messenger.model.UserModel;
import com.prism.messenger.repository.AuthRepo;
import com.prism.messenger.service.AuthService;
import com.prism.messenger.util.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  @Autowired
  private AuthRepo storeUserRepo;
  @Autowired
  private EmailSenderServiceImpl emailSenderService;
  @Autowired
  private PasswordEncoder encoder;
  @Autowired
  private AuthenticationProvider authenticationProvider;

  public void deactivateSession(HttpServletRequest request) {
    request.getSession(false).invalidate();
  }

  public void sessionAuthentication(HttpServletRequest request, String email, String password) {
    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email
        , password);
    Authentication auth = authenticationProvider.authenticate(token);
    SecurityContext context = SecurityContextHolder.getContext();
    context.setAuthentication(auth);
    HttpSession session = request.getSession(true);
    session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
  }

  public UserModel login(UserLoginModel user) throws UserNotFoundException,
      IncorrectPasswordException {
    Auth storedUser = storeUserRepo.findByEmail(user.getEmail());
    boolean isUserNotFound = storedUser == null;
    if (isUserNotFound) {
      throw new UserNotFoundException();
    }
    boolean isPasswordIncorrect = !encoder.matches(user.getPassword(),
        storedUser.getPassword());
    if (isPasswordIncorrect) {
      throw new IncorrectPasswordException();
    }
    return UserModel.toModel(storedUser);
  }

  public void deleteUser(HttpServletRequest request, Authentication authentication, Integer id)
      throws UserNotFoundException, PermissionsException {
    Auth storedUser = AuthUtils.getUser(id, storeUserRepo);
    AuthUtils.checkPermission(authentication, storedUser, storeUserRepo);
    boolean isCurrentUser = storedUser.getEmail().equals(authentication.getName());
    if (isCurrentUser) {
      this.deactivateSession(request);
    }
    storeUserRepo.deleteById(id);
  }

  public UserModel getCurrentUser(Authentication authentication) throws UserNotFoundException {
    String currentUserLogin = authentication.getName();
    Auth storedUser = storeUserRepo.findByEmail(currentUserLogin);
    boolean isUserNotFound = storedUser == null;
    if (isUserNotFound) {
      throw new UserNotFoundException();
    }
    return UserModel.toModel(storedUser);
  }

  public UserModel editUserPassword(Authentication authentication, EditPasswordModel passwords)
      throws PermissionsException, UserNotFoundException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, IncorrectPasswordException {
    Auth storedUser = AuthUtils.getUser(passwords.getId(), storeUserRepo);
    AuthUtils.checkPermission(authentication, storedUser, storeUserRepo);
    boolean isOldPasswordIncorrect = !encoder.matches(passwords.getOldPassword(),
        storedUser.getPassword());
    if (isOldPasswordIncorrect) {
      throw new IncorrectPasswordException();
    }
    passwords.setNewPassword(passwords.getNewPassword().trim());
    AuthUtils.checkPasswordValidity(passwords.getNewPassword());
    storedUser.setPassword(encoder.encode(passwords.getNewPassword()));
    storeUserRepo.save(storedUser);
    return UserModel.toModel(storedUser);
  }

  public UserModel restoreUserPassword(String email, RestorePasswordModel passwords)
      throws ActivationCodeExpireException, IncorrectConfirmPasswordException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, IncorrectConfirmCodeException {
    ActivationCodeModel storedUser = emailSenderService.getUserByEmail(email);
    boolean isPasswordsNotSame =
        !passwords.getConfirmPassword().equals(passwords.getPassword());
    boolean isIncorrectCode = !storedUser.getCode().equals(passwords.getCode());
    AuthUtils.checkPasswordValidity(passwords.getPassword());
    if (isIncorrectCode) {
      throw new IncorrectConfirmCodeException();
    }
    if (isPasswordsNotSame) {
      throw new IncorrectConfirmPasswordException();
    }
    storedUser.setPassword(encoder.encode(passwords.getPassword()));
    storeUserRepo.save(ActivationCodeModel.toAuth(storedUser));
    return UserModel.toModel(ActivationCodeModel.toAuth(storedUser));
  }

  public UserModel saveUserAfterConfirm(ActivationCodeModel user, String code)
      throws IncorrectConfirmCodeException {
    boolean isActivationCodeIncorrect = !code.equals(user.getCode());
    if (isActivationCodeIncorrect) {
      throw new IncorrectConfirmCodeException();
    }
    Auth userEntity = ActivationCodeModel.toAuth(user);
    storeUserRepo.save(userEntity);
    return UserModel.toModel(userEntity);
  }

  public void checkThrottleRequest(HttpServletRequest request, String type)
      throws TooManyAttemptsException {
    Object storedAttempts = request.getSession().getAttribute(type + "-attempts");
    boolean isFirstAttempt = storedAttempts == null;
    if (isFirstAttempt) {
      setFirstAttempt(request, type);
    } else {
      checkThrottleRequests(storedAttempts, request, type);
    }
  }

  private void setFirstAttempt(HttpServletRequest request, String type) {
    Date time = new Date();
    RequestAttemptModel newAttempt = new RequestAttemptModel(type, 1, time.getTime());
    request.getSession().setAttribute(type + "-attempts", newAttempt);
  }

  private void checkThrottleRequests(Object storedAttempts, HttpServletRequest request,
      String type) throws TooManyAttemptsException {
    RequestAttemptModel attemptModel = (RequestAttemptModel) storedAttempts;
    Date time = new Date();
    int limitAttempts = type.equals("login") ? 5 : 1;
    boolean isLimitAttempt = attemptModel.getAttemptCount() >= limitAttempts;
    boolean isAttemptExpired = attemptModel.getLastAttemptTime() + 60000 < time.getTime();
    if (isAttemptExpired) {
      setFirstAttempt(request, type);
    }
    if (isLimitAttempt && !isAttemptExpired) {
      long remainingMillisecondsTime =
          attemptModel.getLastAttemptTime() + 60000 - time.getTime();
      long seconds = (remainingMillisecondsTime / 1000) % 60;
      long minutes = (remainingMillisecondsTime / 1000) / 60;
      String remainingStringTime = String.format("%02d:%02d", minutes, seconds);
      throw new TooManyAttemptsException(remainingStringTime);
    }
    if (!isLimitAttempt) {
      attemptModel.setAttemptCount(attemptModel.getAttemptCount() + 1);
      attemptModel.setLastAttemptTime(time.getTime());
      request.getSession().setAttribute(type + "-attempts", attemptModel);
    }
  }
}
