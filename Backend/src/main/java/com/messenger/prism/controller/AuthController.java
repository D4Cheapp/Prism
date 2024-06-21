package com.messenger.prism.controller;

import com.messenger.prism.exception.PermissionsException;
import com.messenger.prism.exception.TooManyAttemptsException;
import com.messenger.prism.exception.auth.ActivationCodeExpireException;
import com.messenger.prism.exception.auth.UserAlreadyExistException;
import com.messenger.prism.exception.auth.UserNotFoundException;
import com.messenger.prism.exception.auth.email.EmptyEmailException;
import com.messenger.prism.exception.auth.email.IncorectEmailException;
import com.messenger.prism.exception.auth.password.*;
import com.messenger.prism.model.TextResponseModel;
import com.messenger.prism.model.auth.*;
import com.messenger.prism.service.auth.impl.AuthServiceImpl;
import com.messenger.prism.service.auth.impl.EmailSenderServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication")
@Controller
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    @Autowired
    private AuthServiceImpl authService;
    @Autowired
    private EmailSenderServiceImpl emailSenderService;

    @GetMapping("/user")
    public ResponseEntity<UserModel> getCurrentAuthenticatedUser(Authentication authentication) throws UserNotFoundException {
        UserModel currentUser = authService.getCurrentUser(authentication);
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserModel> login(@RequestBody UserLoginModel user,
                                           HttpServletRequest request,
                                           HttpServletResponse response) throws TooManyAttemptsException, UserNotFoundException, IncorrectPasswordException {
        authService.checkTrottleRequest(request, "login");
        UserModel returnedUser = authService.login(user);
        authService.sessionAuthentication(request, response, user.getEmail(), user.getPassword());
        return new ResponseEntity<>(returnedUser, HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<TextResponseModel> sendRegistrationCode(@RequestBody UserRegistrationModel user, HttpServletRequest request) throws EmptyPasswordException, PasswordIsTooWeakException, IncorrectConfirmPasswordException, UserAlreadyExistException, EmptyEmailException, IncorectEmailException, TooLongPasswordException, TooShortPasswordException, TooManyAttemptsException {
        authService.checkTrottleRequest(request, "registration");
        emailSenderService.sendRegitrationCode(user);
        return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Confirmation " +
                "email was sent to: " + user.getEmail(), true), HttpStatus.OK);
    }

    @PostMapping("/restore-password")
    public ResponseEntity<TextResponseModel> sendRestorePasswordEmail(@RequestBody EmailModel email, HttpServletRequest request) throws TooManyAttemptsException, UserNotFoundException {
        authService.checkTrottleRequest(request, "restore-password");
        emailSenderService.sendRestorePasswordCode(email.getEmail());
        return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Restore " + "password "
                + "email was succesfuly sent", true), HttpStatus.OK);
    }

    @PostMapping("/email")
    public ResponseEntity<TextResponseModel> sendEditUserEmailConfirmition(@RequestBody EmailModel email, HttpServletRequest request, Authentication authentication) throws TooManyAttemptsException, UserNotFoundException, UserAlreadyExistException, EmptyEmailException, IncorectEmailException, PermissionsException {
        authService.checkTrottleRequest(request, "edit-email");
        emailSenderService.sendEditUserEmailCode(authentication, email);
        return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Confirmation " + "for "
                + "email change was sent to: " + email.getEmail(), true), HttpStatus.OK);
    }

    @PatchMapping("/registration")
    public ResponseEntity<TextResponseModel> confirmRegistration(AuthConfirmModel confirmModel,
                                                                 HttpServletRequest request) throws ActivationCodeExpireException {
        ActivationCodeModel activationCodeModel =
                emailSenderService.getUserByActivationCode(confirmModel.getCode());
        UserModel returnedUser = authService.saveUserAfterConfirm(activationCodeModel);
        emailSenderService.deleteActivationCode(confirmModel.getCode());
        return new ResponseEntity<>(TextResponseModel.toTextResponseModel("User was " + "created "
                + "successfully", true), HttpStatus.CREATED);
    }

    @PatchMapping("/email")
    public ResponseEntity<TextResponseModel> confirmEmail(AuthConfirmModel confirmModel,
                                                          HttpServletRequest request) throws ActivationCodeExpireException {
        ActivationCodeModel activationCodeModel =
                emailSenderService.getUserByActivationCode(confirmModel.getCode());
        UserModel returnedUser = authService.saveUserAfterConfirm(activationCodeModel);
        emailSenderService.deleteActivationCode(confirmModel.getCode());
        authService.deleteSession(request);
        return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Email " + "was" + " " + "changed successfully", true), HttpStatus.OK);
    }

    @PatchMapping("/restore-password")
    public ResponseEntity<TextResponseModel> confirmPasswordRestore(@RequestBody RestorePasswordModel passwordModel) throws ActivationCodeExpireException, EmptyPasswordException, PasswordIsTooWeakException, IncorrectConfirmPasswordException, TooLongPasswordException, TooShortPasswordException {
        ActivationCodeModel activationCodeModel =
                emailSenderService.getUserByActivationCode(passwordModel.getCode());
        UserModel returnedUser = authService.restoreUserPassword(passwordModel.getCode(),
                activationCodeModel, passwordModel);
        emailSenderService.deleteActivationCode(passwordModel.getCode());
        return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Password " + "was " +
                "changed successfully", true), HttpStatus.OK);
    }

    @PatchMapping("/password")
    public ResponseEntity<UserModel> sendEditUserPasswordConfirmition(@RequestBody EditPasswordModel passwords, Authentication authentication) throws UserNotFoundException, EmptyPasswordException, PasswordIsTooWeakException, IncorrectPasswordException, TooLongPasswordException, PermissionsException, TooShortPasswordException {
        UserModel returnedUser = authService.editUserPassword(authentication, passwords);
        return new ResponseEntity<>(returnedUser, HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<TextResponseModel> logout(Authentication authentication,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        this.logoutHandler.logout(request, response, authentication);
        return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Successful " + "logout"
                , true), HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<TextResponseModel> deleteUser(@PathVariable Integer id,
                                                        Authentication authentication) throws UserNotFoundException, PermissionsException {
        authService.deleteUser(authentication, id);
        return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Successful " +
                "deletion", true), HttpStatus.OK);
    }
}
