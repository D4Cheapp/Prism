package com.messenger.prism.controller;

import com.messenger.prism.model.TextResponseModel;
import com.messenger.prism.model.auth.*;
import com.messenger.prism.service.auth.impl.AuthServiceImpl;
import com.messenger.prism.service.auth.impl.EmailSenderServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<UserModel> getCurrentAuthenticatedUser(Authentication authentication) {
        try {
            UserModel currentUser = authService.getCurrentUser(authentication);
            return ResponseEntity.ok().body(currentUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(UserModel.toModel("Error" + ":" + " " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserModel> login(@RequestBody UserLoginModel user,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        try {
            UserModel returnedUser = authService.login(user);
            authService.sessionAuthentication(request, response, user.getEmail(),
                    user.getPassword());
            return ResponseEntity.ok().body(returnedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(UserModel.toModel("Error" + ":" + " " + e.getMessage()));
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<TextResponseModel> sendRegistrationCode(@RequestBody UserRegistrationModel user, HttpServletRequest request) {
        try {
            emailSenderService.sendRegitrationCode(user);
            return ResponseEntity.ok().body(TextResponseModel.toTextResponseModel("Confirmation " + "email was sent to: " + user.getEmail(), true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(TextResponseModel.toTextResponseModel("Error" + ":" + " " + e.getMessage(), false));
        }
    }

    @PostMapping("/restore-password")
    public ResponseEntity<TextResponseModel> sendRestorePasswordEmail(@RequestBody EmailModel email, HttpServletRequest request) {
        try {
            emailSenderService.sendRestorePasswordCode(email.getEmail());
            return ResponseEntity.ok().body(TextResponseModel.toTextResponseModel("Restore " +
                    "password " + "email was succesfuly sent", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(TextResponseModel.toTextResponseModel("Error" + ":" + " " + e.getMessage(), false));
        }
    }

    @PostMapping("/email")
    public ResponseEntity<TextResponseModel> sendEditUserEmailConfirmition(@RequestBody EmailModel email, HttpServletRequest request, Authentication authentication) {
        try {
            emailSenderService.sendEditUserEmailCode(authentication, email);
            return ResponseEntity.ok().body(TextResponseModel.toTextResponseModel("Confirmation " + "for email change was sent to: " + email.getEmail(), true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(TextResponseModel.toTextResponseModel("Error" + ":" + " " + e.getMessage(), false));
        }
    }

    @PatchMapping("/registration")
    public ResponseEntity<TextResponseModel> confirmRegistration(AuthConfirmModel confirmModel,
                                                                 HttpServletRequest request) {
        try {
            ActivationCodeModel activationCodeModel =
                    emailSenderService.getUserByActivationCode(confirmModel.getCode());
            UserModel returnedUser = authService.saveUserAfterConfirm(activationCodeModel);
            emailSenderService.deleteActivationCode(confirmModel.getCode());
            return ResponseEntity.ok().body(TextResponseModel.toTextResponseModel("User was " +
                    "created successfully", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(TextResponseModel.toTextResponseModel("Error" + ":" + " " + e.getMessage(), false));
        }
    }

    @PatchMapping("/email")
    public ResponseEntity<TextResponseModel> confirmEmail(AuthConfirmModel confirmModel,
                                                          HttpServletRequest request) {
        try {
            ActivationCodeModel activationCodeModel =
                    emailSenderService.getUserByActivationCode(confirmModel.getCode());
            UserModel returnedUser = authService.saveUserAfterConfirm(activationCodeModel);
            emailSenderService.deleteActivationCode(confirmModel.getCode());
            authService.deleteSession(request);
            return ResponseEntity.ok().body(TextResponseModel.toTextResponseModel("Email " + "was"
                    + " changed successfully", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(TextResponseModel.toTextResponseModel("Error" + ":" + " " + e.getMessage(), false));
        }
    }

    @PatchMapping("/restore-password")
    public ResponseEntity<TextResponseModel> confirmPasswordRestore(@RequestBody RestorePasswordModel passwordModel) {
        try {
            ActivationCodeModel activationCodeModel =
                    emailSenderService.getUserByActivationCode(passwordModel.getCode());
            UserModel returnedUser = authService.restoreUserPassword(passwordModel.getCode(),
                    activationCodeModel, passwordModel);
            emailSenderService.deleteActivationCode(passwordModel.getCode());
            return ResponseEntity.ok().body(TextResponseModel.toTextResponseModel("Password " +
                    "was changed successfully", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(TextResponseModel.toTextResponseModel("Error" + ":" + " " + e.getMessage(), false));
        }
    }

    @PatchMapping("/password")
    public ResponseEntity<UserModel> sendEditUserPasswordConfirmition(@RequestBody EditPasswordModel passwords, Authentication authentication) {
        try {
            UserModel returnedUser = authService.editUserPassword(authentication, passwords);
            return ResponseEntity.ok().body(returnedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(UserModel.toModel("Error" + ":" + " " + e.getMessage()));
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<TextResponseModel> logout(Authentication authentication,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response) {
        try {
            this.logoutHandler.logout(request, response, authentication);
            return ResponseEntity.ok().body(TextResponseModel.toTextResponseModel("Successful " + "logout", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(TextResponseModel.toTextResponseModel("Error" + ":" + " " + e.getMessage(), false));
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<TextResponseModel> deleteUser(@PathVariable Integer id,
                                                        Authentication authentication) {
        try {
            authService.deleteUser(authentication, id);
            return ResponseEntity.ok().body(TextResponseModel.toTextResponseModel("Successful " + "deletion", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(TextResponseModel.toTextResponseModel("Error" + ":" + " " + e.getMessage(), false));
        }
    }
}
