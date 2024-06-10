package com.messenger.prism.controller;

import com.messenger.prism.repository.ProfileRepo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User")
@RestController
@RequestMapping("prism/v1/user")
public class UserController {
    @Autowired
    private ProfileRepo repo;
}
