package com.messenger.prism.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthEmailConformitionController {
    @GetMapping("/registration/confirm")
    public String confirmRegistrationTemplate(@RequestParam("code") String code, Model model) {
        model.addAttribute("confirmLabel", "Activate your account");
        model.addAttribute("confirmRegistration", true);
        return "auth/confirmPage";
    }

    @GetMapping("/user/email/confirm")
    public String confirmEmailTemplate(@RequestParam("code") String code, Model model) {
        model.addAttribute("confirmLabel", "Confirm your email");
        model.addAttribute("confirmEmail", true);
        return "auth/confirmPage";
    }

    @GetMapping("/user/restore-password/confirm")
    public String restorePassword(@RequestParam("code") String code, Model model) {
        return "auth/restorePassword";
    }
}
