package com.prism.messenger.advice;

import com.prism.messenger.exception.profile.IncorrectPhoneNumberException;
import com.prism.messenger.exception.profile.PhoneNumberAlreadyExistException;
import com.prism.messenger.exception.profile.ProfileNotExistException;
import com.prism.messenger.exception.profile.StatusIsTooLongException;
import com.prism.messenger.exception.profile.TagAlreadyExistException;
import com.prism.messenger.exception.relation.AddCurrentProfileToCurrentProfileException;
import com.prism.messenger.model.common.TextResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProfileAdvice {

  @ExceptionHandler({IncorrectPhoneNumberException.class, PhoneNumberAlreadyExistException.class,
      StatusIsTooLongException.class, TagAlreadyExistException.class,
      AddCurrentProfileToCurrentProfileException.class})
  public ResponseEntity<TextResponseModel> handleProfileValidationException(Exception exception) {
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Error: " + exception.getMessage(), false),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler({ProfileNotExistException.class})
  public ResponseEntity<TextResponseModel> handleProfileNotExistException(Exception exception) {
    return new ResponseEntity<>(
        TextResponseModel.toTextResponseModel("Error: " + exception.getMessage(), false),
        HttpStatus.NOT_FOUND);
  }
}
