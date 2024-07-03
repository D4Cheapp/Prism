package com.prism.messenger.advice;

import com.prism.messenger.exception.ActivationCodeExpireException;
import com.prism.messenger.exception.IncorrectConfirmCodeException;
import com.prism.messenger.exception.UserAlreadyExistException;
import com.prism.messenger.exception.UserNotFoundException;
import com.prism.messenger.exception.email.EmptyEmailException;
import com.prism.messenger.exception.email.IncorectEmailException;
import com.prism.messenger.exception.password.*;
import com.prism.messenger.model.TextResponseModel;
import jakarta.mail.internet.AddressException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AuthAdvice {
    @ExceptionHandler({ActivationCodeExpireException.class, UserAlreadyExistException.class, EmptyEmailException.class, EmptyPasswordException.class, IncorectEmailException.class, IncorrectConfirmPasswordException.class, IncorrectPasswordException.class, PasswordIsTooWeakException.class, TooLongPasswordException.class, TooShortPasswordException.class, UserNotFoundException.class, AddressException.class, IncorrectConfirmCodeException.class})
    public ResponseEntity<TextResponseModel> handleException(Exception exception) {
        return new ResponseEntity<>(TextResponseModel.toTextResponseModel("Error" + ": " + exception.getMessage(), false), HttpStatus.BAD_REQUEST);
    }

}
