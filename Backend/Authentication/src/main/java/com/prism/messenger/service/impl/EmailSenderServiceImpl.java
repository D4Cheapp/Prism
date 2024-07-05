package com.prism.messenger.service.impl;

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
import com.prism.messenger.repository.ActicationCodeRepo;
import com.prism.messenger.repository.AuthRepo;
import com.prism.messenger.service.auth.impl.auth.EmailSenderService;
import com.prism.messenger.util.AuthUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {

  @Autowired
  private AuthRepo storeUserRepo;
  @Autowired
  private ActicationCodeRepo acticationCodeRepo;
  @Autowired
  private JavaMailSender javaMailSender;
  @Autowired
  private PasswordEncoder encoder;
  @Value("${spring.mail.username}")
  private String fromEmail;

  public void saveActivationCode(Auth account, String message) {
    String code = String.valueOf((int) ((Math.random() * (9999 - 1000)) + 1000));
    SimpleMailMessage mail = new SimpleMailMessage();
    mail.setFrom(fromEmail);
    mail.setTo(account.getEmail());
    mail.setSubject("Prism activation code");
    mail.setText(message + "\nCode: " + code);
    acticationCodeRepo.saveActivationCode(new ActivationCodeModel(account, code));
    javaMailSender.send(mail);
  }

  public ActivationCodeModel getUserByEmail(String email) throws ActivationCodeExpireException {
    return acticationCodeRepo.findActivationCodeByEmail(email);
  }

  public void deleteActivationCode(String email) {
    acticationCodeRepo.deleteActivationCode(email);
  }

  public void sendEditUserEmailCode(EmailModel editData, Authentication authentication,
      HttpServletRequest request)
      throws PermissionsException, UserNotFoundException, UserAlreadyExistException, EmptyEmailException, IncorectEmailException {
    Optional<Auth> storedUser = storeUserRepo.findById(editData.getId());
    Auth changedEmailUser = storeUserRepo.findByEmail(editData.getEmail());
    boolean isUserNotFound = storedUser.isEmpty();
    boolean isLoginAlreadyExists = changedEmailUser != null;
    AuthUtils.checkPermission(authentication, storedUser, storeUserRepo);
    if (isUserNotFound) {
      throw new UserNotFoundException();
    }
    if (isLoginAlreadyExists) {
      throw new UserAlreadyExistException();
    }
    AuthUtils.checkEmailValidity(editData.getEmail());
    storedUser.get().setEmail(editData.getEmail());
    request.getSession().setAttribute("confirmEmail", editData.getEmail());
    this.saveActivationCode(storedUser.get(), "Activation code for confirm your email ");
  }

  public void sendRegitrationCode(UserRegistrationModel user, HttpServletRequest request)
      throws IncorrectConfirmPasswordException, UserAlreadyExistException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, EmptyEmailException, IncorectEmailException {
    boolean isDeveloperFieldMissing = user.getIsDeveloper() == null;
    boolean isUserAlreadyExists = storeUserRepo.findByEmail(user.getEmail()) != null;
    boolean isConfirmPasswordInorrect = !(user.getConfirmPassword() != null
        && user.getConfirmPassword().equals(user.getPassword()));
    if (isDeveloperFieldMissing) {
      user.setIsDeveloper(false);
    }
    if (isUserAlreadyExists) {
      throw new UserAlreadyExistException();
    }
    if (isConfirmPasswordInorrect) {
      throw new IncorrectConfirmPasswordException();
    }
    AuthUtils.checkEmailValidity(user.getEmail());
    AuthUtils.checkPasswordValidity(user.getPassword());
    Auth userEntity = UserRegistrationModel.toEntity(user, encoder);
    request.getSession().setAttribute("confirmEmail", userEntity.getEmail());
    this.saveActivationCode(userEntity, "Activation code for registration");
  }

  public void sendRestorePasswordCode(String email, HttpServletRequest request)
      throws UserNotFoundException {
    Auth currentUser = storeUserRepo.findByEmail(email);
    boolean isUserNotFound = currentUser == null;
    if (isUserNotFound) {
      throw new UserNotFoundException();
    }
    request.getSession().setAttribute("confirmEmail", currentUser.getEmail());
    this.saveActivationCode(currentUser, "Restore password code");
  }
}
