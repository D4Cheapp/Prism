package com.prism.messenger.controller;

import static org.springframework.http.HttpStatus.OK;

import com.prism.messenger.exception.chat.ChatAlreadyExistException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.model.chat.ChatModel;
import com.prism.messenger.model.profile.TagModel;
import com.prism.messenger.service.chat.impl.ChatServiceImpl;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chat")
@RestController
@RequestMapping("/chat")
public class ChatController {

  @Autowired
  private ChatServiceImpl chatService;

  @Operation(summary = "Create chat with user")
  @PostMapping("/create")
  public ResponseEntity<ChatModel> createChat(@RequestBody TagModel interlocutor,
      Authentication authentication)
      throws ProfileNotExistException, ChatAlreadyExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    ChatModel chat = chatService.createChat(email, interlocutor.getTag());
    return new ResponseEntity<>(chat, OK);
  }
}
