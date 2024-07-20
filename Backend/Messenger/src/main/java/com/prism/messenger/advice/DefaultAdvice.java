package com.prism.messenger.advice;

import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.rabbitMQ.IncorrectMessageActionException;
import com.prism.messenger.model.common.TextResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultAdvice {

  @ExceptionHandler({Exception.class, IncorrectMessageActionException.class,
      PermissionsException.class})
  public ResponseEntity<TextResponseModel> defaultExceptionHandler(Exception exception) {
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Error: " + exception.getMessage(), false),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
