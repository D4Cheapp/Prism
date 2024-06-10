package com.messenger.prism.controller;

import com.messenger.prism.entity.Auth;
import com.messenger.prism.model.TextResponseModel;
import com.messenger.prism.model.auth.UserLoginModel;
import com.messenger.prism.model.auth.UserModel;
import com.messenger.prism.model.auth.UserRegistrationModel;
import com.messenger.prism.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication")
@RestController
@RequestMapping("prism/v1")
public class AuthController {
    @Autowired
    private AuthService service;
    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    @PostMapping("/registration")
    public ResponseEntity<UserModel> registration(@RequestBody UserRegistrationModel user,
                                                  HttpServletRequest request, HttpServletResponse response) {
        try {
            UserModel returnedUser = service.regitration(user);
            service.authentication(request, response, user.getLogin(), user.getPassword());
            return ResponseEntity.ok().body(returnedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(UserModel.toModel("Error: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserModel> login(@RequestBody UserLoginModel user,
                                           HttpServletRequest request, HttpServletResponse response) {
        try {
            UserModel returnedUser = service.login(user);
            service.authentication(request, response, user.getLogin(), user.getPassword());
            return ResponseEntity.ok().body(returnedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(UserModel.toModel("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/logout")
    public ResponseEntity<TextResponseModel> logout(Authentication authentication,
                                                    HttpServletRequest request, HttpServletResponse response) {
        try {
            this.logoutHandler.logout(request, response, authentication);
            return ResponseEntity.ok().body(TextResponseModel.toTextResponseModel("Successful logout", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(TextResponseModel.toTextResponseModel("Error: " + e.getMessage(), false));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<UserModel> getCurrentUser(Authentication authentication) {
        try {
            UserModel currentUser = service.getCurrentUser(authentication);
            return ResponseEntity.ok().body(currentUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(UserModel.toModel("Error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<TextResponseModel> deleteUser(@PathVariable Integer id, Authentication authentication) {
        try {
            service.deleteUser(authentication, id);
            return ResponseEntity.ok().body(TextResponseModel.toTextResponseModel("Successful deletion", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(TextResponseModel.toTextResponseModel("Error: " + e.getMessage(), false));
        }
    }

    @PatchMapping("/user/{id}/login")
    public ResponseEntity<UserModel> editUserLogin(@PathVariable Integer id, @RequestBody Auth login, Authentication authentication) {
        try {
            UserModel returnedUser = service.editUserLogin(authentication, id, login);
            return ResponseEntity.ok().body(returnedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(UserModel.toModel("Error: " + e.getMessage()));
        }
    }

    @PatchMapping("/user/{id}/password")
    public ResponseEntity<UserModel> editUserPassword(@PathVariable Integer id, @RequestBody Auth password, Authentication authentication) {
        try {
            UserModel returnedUser = service.editUserPassword(authentication, id, password);
            return ResponseEntity.ok().body(returnedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(UserModel.toModel("Error: " + e.getMessage()));
        }
    }
}
