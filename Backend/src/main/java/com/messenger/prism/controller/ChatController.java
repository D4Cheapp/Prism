package com.messenger.prism.controller;

import com.messenger.prism.repository.ChatRepo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chat")
@RestController
@RequestMapping("prism/v1/chat")
public class ChatController {
    @Autowired
    private ChatRepo repo;
}
