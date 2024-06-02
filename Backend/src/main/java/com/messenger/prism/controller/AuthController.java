package com.messenger.prism.controller;

import com.messenger.prism.entity.AuthEntity;
import com.messenger.prism.model.auth.UserLoginModel;
import com.messenger.prism.model.auth.UserModel;
import com.messenger.prism.model.auth.UserRegistrationModel;
import com.messenger.prism.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("prism/v1")
//TODO: Реализация через cookie
public class AuthController {
    @Autowired
    private AuthService service;

    @GetMapping
    public ResponseEntity<String> greeting() {
        return ResponseEntity.ok().body("Hi");
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserRegistrationModel user) {
        try {
            UserModel returnedUser = service.regitration(user);
            return ResponseEntity.ok().body(returnedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginModel user) {
        try {
            UserModel returnedUser = service.login(user);
            return ResponseEntity.ok().body(returnedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    //TODO logout
//    @DeleteMapping("/logout")
//    public ResponseEntity logout(HttpSession session) {
//        try {
//            session.invalidate();
//            return ResponseEntity.ok().body("Successful logout");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//    }

    //TODO get current user
//    @GetMapping("/user")
//    public ResponseEntity getCurrentUser() {
//        try {
//            service.getCurrentUser();
//            return ResponseEntity.ok().body("Success");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
//        }
//     }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            service.deleteUser(id);
            return ResponseEntity.ok().body("Successful deletion");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/user/{id}/login")
    public ResponseEntity<?> editUserLogin(@PathVariable Integer id, @RequestBody AuthEntity login) {
        try {
            UserModel returnedUser = service.editUserLogin(id, login);
            return ResponseEntity.ok().body(returnedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PatchMapping("/user/{id}/password")
    public ResponseEntity<?> editUserPassword(@PathVariable Integer id, @RequestBody AuthEntity password) {
        try {
            UserModel returnedUser = service.editUserPassword(id, password);
            return ResponseEntity.ok().body(returnedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}
