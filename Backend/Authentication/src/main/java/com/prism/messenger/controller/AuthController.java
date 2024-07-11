package com.prism.messenger.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.prism.messenger.exception.ActivationCodeExpireException;
import com.prism.messenger.exception.IncorrectConfirmCodeException;
import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.TooManyAttemptsException;
import com.prism.messenger.exception.UserAlreadyExistException;
import com.prism.messenger.exception.UserNotFoundException;
import com.prism.messenger.exception.email.EmptyEmailException;
import com.prism.messenger.exception.email.IncorectEmailException;
import com.prism.messenger.exception.password.EmptyPasswordException;
import com.prism.messenger.exception.password.IncorrectConfirmPasswordException;
import com.prism.messenger.exception.password.IncorrectPasswordException;
import com.prism.messenger.exception.password.PasswordIsTooWeakException;
import com.prism.messenger.exception.password.TooLongPasswordException;
import com.prism.messenger.exception.password.TooShortPasswordException;
import com.prism.messenger.model.ActivationCodeModel;
import com.prism.messenger.model.EditPasswordModel;
import com.prism.messenger.model.EmailModel;
import com.prism.messenger.model.RestorePasswordModel;
import com.prism.messenger.model.TextResponseModel;
import com.prism.messenger.model.UserLoginModel;
import com.prism.messenger.model.UserModel;
import com.prism.messenger.model.UserRegistrationModel;
import com.prism.messenger.service.impl.AuthServiceImpl;
import com.prism.messenger.service.impl.EmailSenderServiceImpl;
import com.prism.messenger.service.impl.RabbitMQServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  private AuthServiceImpl authService;
  @Autowired
  private EmailSenderServiceImpl emailSenderService;
  @Autowired
  private RabbitMQServiceImpl rabbitMQService;

  @Operation(summary = "Get current authenticated user")
  @GetMapping("/user")
  public ResponseEntity<UserModel> getCurrentAuthenticatedUser(Authentication authentication)
      throws UserNotFoundException {
    UserModel currentUser = authService.getCurrentUser(authentication);
    return new ResponseEntity<>(currentUser, OK);
  }

  @Operation(summary = "Login")
  @PostMapping("/login")
  public ResponseEntity<UserModel> login(@RequestBody UserLoginModel user,
      HttpServletRequest request)
      throws TooManyAttemptsException, UserNotFoundException, IncorrectPasswordException {
    authService.checkThrottleRequest(request, "login");
    UserModel returnedUser = authService.login(user);
    authService.sessionAuthentication(request, user.getEmail(), user.getPassword());
    return new ResponseEntity<>(returnedUser, OK);
  }

  @Operation(summary = "Registration")
  @PostMapping("/registration")
  public ResponseEntity<TextResponseModel> sendRegistrationCode(
      @RequestBody UserRegistrationModel user, HttpServletRequest request)
      throws TooManyAttemptsException, EmptyPasswordException, PasswordIsTooWeakException, IncorrectConfirmPasswordException, UserAlreadyExistException, EmptyEmailException, IncorectEmailException, TooLongPasswordException, TooShortPasswordException {
    authService.checkThrottleRequest(request, "registration");
    emailSenderService.sendRegistrationCode(user, request);
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Confirmation email was sent to: " + user.getEmail(),
            true), OK);
  }

  @Operation(summary = "Send restore password email")
  @PostMapping("/restore-password")
  public ResponseEntity<TextResponseModel> sendRestorePasswordEmail(@RequestBody EmailModel email,
      HttpServletRequest request) throws TooManyAttemptsException, UserNotFoundException {
    authService.checkThrottleRequest(request, "restore-password");
    emailSenderService.sendRestorePasswordCode(email.getEmail(), request);
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Restore password email was successfully sent", true),
        OK);
  }

  @Operation(summary = "Send edit user email confirmation")
  @PostMapping("/email")
  public ResponseEntity<TextResponseModel> sendEditUserEmailConfirmation(
      @RequestBody EmailModel email, HttpServletRequest request, Authentication authentication)
      throws TooManyAttemptsException, UserNotFoundException, UserAlreadyExistException, EmptyEmailException, IncorectEmailException, PermissionsException {
    authService.checkThrottleRequest(request, "edit-email");
    emailSenderService.sendEditUserEmailCode(email, authentication, request);
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel(
        "Confirmation for email change was sent to: " + email.getEmail(), true), OK);
  }

  @Transactional
  @Operation(summary = "Confirm registration")
  @PatchMapping("/registration")
  public ResponseEntity<UserModel> confirmRegistration(
      @RequestParam("code") String code, HttpServletRequest request)
      throws ActivationCodeExpireException, IncorrectConfirmCodeException {
    String confirmEmail = (String) request.getSession().getAttribute("confirmEmail");
    ActivationCodeModel activationCodeModel = emailSenderService.getUserByEmail(confirmEmail);
    UserModel returnedUser = authService.saveUserAfterConfirm(activationCodeModel,
        code);
    rabbitMQService.createUserProfile(confirmEmail);
    emailSenderService.deleteActivationCode(confirmEmail);
    request.getSession().removeAttribute("confirmEmail");
    return new ResponseEntity<>(returnedUser, CREATED);
  }

  @Operation(summary = "Confirm edit user email")
  @PatchMapping("/email")
  public ResponseEntity<TextResponseModel> confirmEmail(@RequestParam("code") String code,
      HttpServletRequest request)
      throws ActivationCodeExpireException, IncorrectConfirmCodeException {
    String confirmEmail = (String) request.getSession().getAttribute("confirmEmail");
    ActivationCodeModel activationCodeModel = emailSenderService.getUserByEmail(confirmEmail);
    authService.saveUserAfterConfirm(activationCodeModel, code);
    emailSenderService.deleteActivationCode(confirmEmail);
    rabbitMQService.changeProfileEmail(activationCodeModel.getEmail(), confirmEmail);
    request.getSession().removeAttribute("confirmEmail");
    authService.deactivateSession(request);
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Email was changed successfully", true),
        OK);
  }

  @Operation(summary = "Confirm password restore")
  @PatchMapping("/restore-password")
  public ResponseEntity<UserModel> confirmPasswordRestore(
      @RequestBody RestorePasswordModel passwordModel, HttpServletRequest request)
      throws ActivationCodeExpireException, EmptyPasswordException, PasswordIsTooWeakException, IncorrectConfirmPasswordException, TooLongPasswordException, TooShortPasswordException, IncorrectConfirmCodeException {
    String confirmEmail = (String) request.getSession().getAttribute("confirmEmail");
    UserModel returnedUser = authService.restoreUserPassword(confirmEmail, passwordModel);
    emailSenderService.deleteActivationCode(passwordModel.getCode());
    request.getSession().removeAttribute("confirmEmail");
    return new ResponseEntity<>(returnedUser, OK);
  }

  @Operation(summary = "Edit user password")
  @PatchMapping("/password")
  public ResponseEntity<UserModel> sendEditUserPasswordConfirmation(
      @RequestBody EditPasswordModel passwords, Authentication authentication)
      throws UserNotFoundException, EmptyPasswordException, PasswordIsTooWeakException, IncorrectPasswordException, TooLongPasswordException, PermissionsException, TooShortPasswordException {
    UserModel returnedUser = authService.editUserPassword(authentication, passwords);
    return new ResponseEntity<>(returnedUser, OK);
  }

  @Operation(summary = "Logout")
  @DeleteMapping("/logout")
  public ResponseEntity<TextResponseModel> logout(HttpServletRequest request) {
    authService.deactivateSession(request);
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Successful logout", true), OK);
  }

  @Operation(summary = "Delete user")
  @DeleteMapping("/user")
  public ResponseEntity<TextResponseModel> deleteUser(@RequestParam("userId") int userId,
      Authentication authentication, HttpServletRequest request)
      throws UserNotFoundException, PermissionsException {
    authService.deleteUser(request, authentication, userId);
    rabbitMQService.deleteUserProfile(authentication.getName());
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Successful deletion", true), OK);
  }
}
