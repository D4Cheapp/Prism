package com.prism.messenger.util;


import com.prism.messenger.entity.Auth;
import com.prism.messenger.exception.PermissionsException;
import com.prism.messenger.exception.UserNotFoundException;
import com.prism.messenger.exception.email.EmptyEmailException;
import com.prism.messenger.exception.email.IncorectEmailException;
import com.prism.messenger.exception.password.EmptyPasswordException;
import com.prism.messenger.exception.password.PasswordIsTooWeakException;
import com.prism.messenger.exception.password.TooLongPasswordException;
import com.prism.messenger.exception.password.TooShortPasswordException;
import com.prism.messenger.repository.AuthRepo;
import java.util.Objects;
import java.util.Optional;
import org.springframework.security.core.Authentication;

public class AuthUtils {

  public static Auth getUser(Integer id, AuthRepo storeUserRepo) throws UserNotFoundException {
    Optional<Auth> storedUser = storeUserRepo.findById(id);
    boolean isUserNotFound = storedUser.isEmpty();
    if (isUserNotFound) {
      throw new UserNotFoundException();
    }
    return storedUser.get();
  }

  public static void checkPermission(Authentication authentication, Auth storedUser,
      AuthRepo storeUserRepo) throws PermissionsException {
    Auth currentUser = storeUserRepo.findByEmail(authentication.getName());
    boolean isCurrentUserNotDeveloper = !currentUser.getRole().equals("DEVELOPER");
    boolean isNotCurrentUserToDelete = !(Objects.equals(currentUser.getId(), storedUser.getId()));
    boolean isUserHaveNotPermission = isCurrentUserNotDeveloper && isNotCurrentUserToDelete;
    if (isUserHaveNotPermission) {
      throw new PermissionsException();
    }
  }

  public static void checkPasswordValidity(String password)
      throws TooLongPasswordException, TooShortPasswordException, EmptyPasswordException, PasswordIsTooWeakException {
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
    boolean isPasswordIncludesLowerLetters = password.matches(".*[a-zа-я].*");
    boolean isPasswordIncludesNumbers = password.matches(".*[0-9].*");
    boolean isPasswordIncludesSpecialCharacters = password.matches(".*[!@#$%^&*()].*");
    if (!isPasswordIncludesCapitalLetters) {
      throw new PasswordIsTooWeakException("capital letter");
    }
    if (!isPasswordIncludesLowerLetters) {
      throw new PasswordIsTooWeakException("lower letter");
    }
    if (!isPasswordIncludesNumbers) {
      throw new PasswordIsTooWeakException("number");
    }
    if (!isPasswordIncludesSpecialCharacters) {
      throw new PasswordIsTooWeakException("special character");
    }
  }

  public static void checkEmailValidity(String email)
      throws EmptyEmailException, IncorectEmailException {
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
