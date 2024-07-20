package com.prism.messenger.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Message")
@RestController
@RequestMapping("/message")
public class MessageController {

  @MessageMapping("/dialog/{dialogId}")
  @SendTo("/broker/dialog/{dialogId}")
  public void sendGroupMessage(@DestinationVariable String dialogId, String message) {
  }

}
