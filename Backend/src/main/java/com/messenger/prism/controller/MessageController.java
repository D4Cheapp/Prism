package com.messenger.prism.controller;

import com.messenger.prism.repository.MessageRepo;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Message")
@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    private MessageRepo repo;
}
