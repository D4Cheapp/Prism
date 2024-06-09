package com.messenger.prism.controller;

import com.messenger.prism.repository.ChatRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("prism/v1/chat")
public class ChatController {
    @Autowired
    private ChatRepo repo;
}
