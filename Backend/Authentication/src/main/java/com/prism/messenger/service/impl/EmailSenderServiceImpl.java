package com.prism.messenger.service.impl;

import com.prism.messenger.entity.Auth;
import com.prism.messenger.exception.ActivationCodeExpireException;
import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.UserAlreadyExistException;
import com.prism.messenger.exception.UserNotFoundException;
import com.prism.messenger.exception.email.EmailServiceError;
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
import com.prism.messenger.repository.ActivationCodeRepo;
import com.prism.messenger.repository.AuthRepo;
import com.prism.messenger.service.EmailSenderService;
import com.prism.messenger.util.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

  @Autowired
  private AuthRepo storeUserRepo;
  @Autowired
  private ActivationCodeRepo activationCodeRepo;
  @Autowired
  private JavaMailSender javaMailSender;
  @Autowired
  private TemplateEngine templateEngine;
  @Autowired
  private PasswordEncoder encoder;
  @Value("${spring.mail.username}")
  private String fromEmail;

  public void saveActivationCode(Auth account, String title) throws EmailServiceError {
    String code = String.valueOf((int) ((Math.random() * (9999 - 1000)) + 1000));
    MimeMessagePreparator mail = mimeMessage -> {
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
      message.setTo(account.getEmail());
      message.setFrom(fromEmail);
      message.setSubject("Prism confirm code");
      Context context = new Context();
      context.setVariable("code", code);
      context.setVariable("title", title);
      String html = templateEngine.process("confirmCodeEmail.html", context);
      message.setText(html, true);
    };
    activationCodeRepo.saveActivationCode(new ActivationCodeModel(account, code));
    try {
      javaMailSender.send(mail);
    } catch (Exception e) {
      throw new EmailServiceError();
    }
  }

  public ActivationCodeModel getUserByEmail(String email) throws ActivationCodeExpireException {
    return activationCodeRepo.findActivationCodeByEmail(email);
  }

  public void deleteActivationCode(String email) {
    activationCodeRepo.deleteActivationCode(email);
  }

  public void sendEditUserEmailCode(ChangeEmailModel editData, Authentication authentication,
      HttpServletRequest request)
      throws UserNotFoundException, PermissionsException, UserAlreadyExistException,
      EmptyEmailException, IncorectEmailException, EmailServiceError {
    Auth storedUser = AuthUtils.getUser(editData.getId(), storeUserRepo);
    Auth changedEmailUser = storeUserRepo.findByEmail(editData.getEmail());
    boolean isEmailAlreadyExists = changedEmailUser != null;
    AuthUtils.checkPermission(authentication, storedUser, storeUserRepo);
    if (isEmailAlreadyExists) {
      throw new UserAlreadyExistException();
    }
    AuthUtils.checkEmailValidity(editData.getEmail());
    storedUser.setEmail(editData.getEmail());
    request.getSession().setAttribute("confirmEmail", editData.getEmail());
    this.saveActivationCode(storedUser, "Activation code for confirm your email ");
  }

  public void sendRegistrationCode(UserRegistrationModel user, HttpServletRequest request)
      throws IncorrectConfirmPasswordException, UserAlreadyExistException, EmptyPasswordException,
      PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException,
      EmptyEmailException, IncorectEmailException, EmailServiceError {
    boolean isDeveloperFieldMissing = user.getIsDeveloper() == null;
    boolean isUserAlreadyExists = storeUserRepo.findByEmail(user.getEmail()) != null;
    boolean isConfirmPasswordIncorrect = !(user.getConfirmPassword() != null
        && user.getConfirmPassword().equals(user.getPassword()));
    if (isDeveloperFieldMissing) {
      user.setIsDeveloper(false);
    }
    if (isUserAlreadyExists) {
      throw new UserAlreadyExistException();
    }
    if (isConfirmPasswordIncorrect) {
      throw new IncorrectConfirmPasswordException();
    }
    AuthUtils.checkEmailValidity(user.getEmail());
    AuthUtils.checkPasswordValidity(user.getPassword());
    Auth userEntity = UserRegistrationModel.toEntity(user, encoder);
    request.getSession().setAttribute("confirmEmail", userEntity.getEmail());
    this.saveActivationCode(userEntity, "Activation code for registration");
  }

  public void sendRestorePasswordCode(String email, HttpServletRequest request)
      throws UserNotFoundException, EmailServiceError {
    Auth currentUser = storeUserRepo.findByEmail(email);
    boolean isUserNotFound = currentUser == null;
    if (isUserNotFound) {
      throw new UserNotFoundException();
    }
    request.getSession().setAttribute("confirmEmail", currentUser.getEmail());
    this.saveActivationCode(currentUser, "Restore password code");
  }
}
