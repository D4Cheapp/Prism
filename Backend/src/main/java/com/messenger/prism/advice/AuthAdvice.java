package com.messenger.prism.advice;

import com.messenger.prism.exception.auth.ActivationCodeExpireException;
import com.messenger.prism.exception.auth.UserAlreadyExistException;
import com.messenger.prism.exception.auth.UserNotFoundException;
import com.messenger.prism.exception.auth.email.EmptyEmailException;
import com.messenger.prism.exception.auth.email.IncorectEmailException;
import com.messenger.prism.exception.auth.password.*;
import com.messenger.prism.model.TextResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthAdvice {
    @ExceptionHandler({ActivationCodeExpireException.class, UserAlreadyExistException.class,
            EmptyEmailException.class, EmptyPasswordException.class, IncorectEmailException.class
            , IncorrectConfirmPasswordException.class, IncorrectPasswordException.class,
            PasswordIsTooWeakException.class, TooLongPasswordException.class,
            TooShortPasswordException.class, UserNotFoundException.class})
    public ResponseEntity<TextResponseModel> handleException(Exception exception) {
        return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Error" + ": " + exception.getMessage(), false), HttpStatus.BAD_REQUEST);
    }

}
