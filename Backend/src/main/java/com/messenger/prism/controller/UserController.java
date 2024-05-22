package com.messenger.prism.controller;

import com.messenger.prism.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("prism/v1/user")
public class UserController {
    @Autowired
    private UserRepo repo;
}
