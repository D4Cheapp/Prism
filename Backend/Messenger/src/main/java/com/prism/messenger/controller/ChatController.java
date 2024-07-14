package com.prism.messenger.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.chat.ChatAlreadyExistException;
import com.prism.messenger.exception.chat.ChatCreatingException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.model.DialogFilesPaginationListModel;
import com.prism.messenger.model.DialogIdModel;
import com.prism.messenger.model.FileListModel;
import com.prism.messenger.model.PaginationListModel;
import com.prism.messenger.model.TextResponseModel;
import com.prism.messenger.model.chat.ChatListReceiveModel;
import com.prism.messenger.model.chat.ChatModel;
import com.prism.messenger.model.profile.ProfileTagModel;
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
import org.springframework.web.bind.annotation.GetMapping;
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
  @PostMapping
  public ResponseEntity<ChatModel> createChat(
      @RequestBody ProfileTagModel interlocutorTag,
      Authentication authentication)
      throws ProfileNotExistException, ChatAlreadyExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException, ChatCreatingException {
    String email = authentication.getName();
    ChatModel chat = chatService.createChat(email, interlocutorTag.getTag());
    return new ResponseEntity<>(chat, CREATED);
  }

  @Operation(summary = "Delete chat")
  @DeleteMapping
  public ResponseEntity<TextResponseModel> deleteChat(@RequestBody DialogIdModel chatId,
      Authentication authentication)
      throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, PermissionsException, InternalException {
    String email = authentication.getName();
    chatService.deleteChat(email, chatId.getDialogId());
    return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Chat deleted", true), OK);
  }

  @Operation(summary = "Get list of chats")
  @GetMapping("/list")
  public ResponseEntity<ChatListReceiveModel> getChatList(
      @RequestBody PaginationListModel paginationListModel, Authentication authentication)
      throws ProfileNotExistException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    Integer page = paginationListModel.getPage();
    Integer size = paginationListModel.getSize();
    ChatListReceiveModel chatList = chatService.getChatList(email, page, size);
    return new ResponseEntity<>(chatList, OK);
  }

  @Operation(summary = "Get all files of the group with pagination")
  @GetMapping("/files")
  public ResponseEntity<FileListModel> getGroupFiles(
      @RequestBody DialogFilesPaginationListModel paginationListModel,
      Authentication authentication)
      throws PermissionsException, ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
    String email = authentication.getName();
    String dialogId = paginationListModel.getDialogId();
    Integer page = paginationListModel.getPage();
    Integer size = paginationListModel.getSize();
    FileListModel files = chatService.getChatFiles(email, dialogId, page, size);
    return new ResponseEntity<>(files, OK);
  }
}
