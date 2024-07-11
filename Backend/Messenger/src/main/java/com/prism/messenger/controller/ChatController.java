package com.prism.messenger.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.chat.ChatAlreadyExistException;
import com.prism.messenger.exception.chat.ChatCreatingException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.model.TextResponseModel;
import com.prism.messenger.model.dialog.ChatModel;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Chat")
@RestController
@RequestMapping("/chat")
public class ChatController {

  @Autowired
  private ChatServiceImpl chatService;

  @Operation(summary = "Create chat with user")
  @PostMapping
  public ResponseEntity<ChatModel> createChat(
      @RequestParam("interlocutorTag") String interlocutorTag,
      Authentication authentication)
      throws ProfileNotExistException, ChatAlreadyExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, ChatCreatingException {
    String email = authentication.getName();
    ChatModel chat = chatService.createChat(email, interlocutorTag);
    return new ResponseEntity<>(chat, CREATED);
  }

  @Operation(summary = "Delete chat")
  @DeleteMapping
  public ResponseEntity<TextResponseModel> deleteChat(@RequestParam("chatId") String chatId,
      Authentication authentication)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, PermissionsException, InternalException {
    String email = authentication.getName();
    chatService.deleteChat(email, chatId);
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Chat deleted", true), OK);
  }
}
