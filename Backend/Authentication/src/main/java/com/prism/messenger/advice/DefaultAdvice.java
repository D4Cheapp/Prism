package com.prism.messenger.advice;

import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.TooManyAttemptsException;
import com.prism.messenger.model.TextResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultAdvice {

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<TextResponseModel> defaultExceptionHandler(Exception exception) {
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Error: " + exception.getMessage(), false),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(PermissionsException.class)
  public ResponseEntity<TextResponseModel> defaultExceptionHandler(PermissionsException exception) {
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Error: " + exception.getMessage(), false),
        HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(TooManyAttemptsException.class)
  public ResponseEntity<TextResponseModel> handleException(TooManyAttemptsException exception) {
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Error: " + exception.getMessage(), false),
        HttpStatus.TOO_MANY_REQUESTS);
  }
}
