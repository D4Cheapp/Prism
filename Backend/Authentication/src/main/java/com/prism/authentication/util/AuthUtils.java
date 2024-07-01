package com.prism.authentication.util;


import com.prism.authentication.entity.Auth;
import com.prism.authentication.exception.PermissionsException;
import com.prism.authentication.exception.email.EmptyEmailException;
import com.prism.authentication.exception.password.EmptyPasswordException;
import com.prism.authentication.exception.email.IncorectEmailException;
import com.prism.authentication.exception.password.PasswordIsTooWeakException;
import com.prism.authentication.exception.password.TooLongPasswordException;
import com.prism.authentication.exception.password.TooShortPasswordException;
import com.prism.authentication.repository.AuthRepo;
import org.springframework.security.core.Authentication;

import java.util.Objects;
import java.util.Optional;

public class AuthUtils {
    public static void checkPermission(Authentication authentication, Optional<Auth> storedUser, AuthRepo storeUserRepo) throws PermissionsException {
        Auth currentUser = storeUserRepo.findByEmail(authentication.getName());
        boolean isCurrentUserNotDeveloper = !currentUser.getRole().equals("DEVELOPER");
        boolean isNotCurrentUserToDelete = storedUser.isPresent() && !(Objects.equals(currentUser.getId(), storedUser.get().getId()));
        boolean isUserHaveNotPermission = isCurrentUserNotDeveloper && isNotCurrentUserToDelete;
        if (isUserHaveNotPermission) {
            throw new PermissionsException();
        }
    }

    public static void checkPasswordValidity(String password) throws TooLongPasswordException, TooShortPasswordException, EmptyPasswordException, PasswordIsTooWeakException {
        boolean isPasswordShort = password.length() < 6;
        boolean isPasswordTooLong = password.length() > 25;
        boolean isPasswordEmpty = password.isEmpty();
        if (isPasswordEmpty) {
            throw new EmptyPasswordException();
        }
        if (isPasswordShort) {
            throw new TooShortPasswordException();
        }
        if (isPasswordTooLong) {
            throw new TooLongPasswordException();
        }
        AuthUtils.isPasswordTooWeak(password);
    }

    public static void isPasswordTooWeak(String password) throws PasswordIsTooWeakException {
        boolean isPasswordIncludesCapitalLetters = password.matches(".*[A-ZА-Я].*");
        boolean isPasswordIncluidesLowerLetters = password.matches(".*[a-zа-я].*");
        boolean isPasswordIncluidesNumbers = password.matches(".*[0-9].*");
        boolean isPasswordIncluidesSpecialCharacters = password.matches(".*[!@#$%^&*()].*");
        if (!isPasswordIncludesCapitalLetters) {
            throw new PasswordIsTooWeakException("capital letter");
        }
        if (!isPasswordIncluidesLowerLetters) {
            throw new PasswordIsTooWeakException("lower letter");
        }
        if (!isPasswordIncluidesNumbers) {
            throw new PasswordIsTooWeakException("number");
        }
        if (!isPasswordIncluidesSpecialCharacters) {
            throw new PasswordIsTooWeakException("special character");
        }
    }

    public static void checkEmailValidity(String email) throws EmptyEmailException, IncorectEmailException {
        boolean isEmailIncorrect = !email.matches(".+@.+\\..+");
        boolean isEmailEmpty = email.isEmpty();
        if (isEmailEmpty) {
            throw new EmptyEmailException();
        }
        if (isEmailIncorrect) {
            throw new IncorectEmailException();
        }
    }
}
