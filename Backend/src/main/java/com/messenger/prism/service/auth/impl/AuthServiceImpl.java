package com.messenger.prism.service.auth.impl;

import com.messenger.prism.entity.Auth;
import com.messenger.prism.exception.PermissionsException;
import com.messenger.prism.exception.auth.ActivationCodeExpireException;
import com.messenger.prism.exception.auth.UserAlreadyExistException;
import com.messenger.prism.exception.auth.UserNotFoundException;
import com.messenger.prism.exception.auth.email.EmptyEmailException;
import com.messenger.prism.exception.auth.email.EmptyPasswordException;
import com.messenger.prism.exception.auth.email.IncorectEmailException;
import com.messenger.prism.exception.auth.password.*;
import com.messenger.prism.model.auth.*;
import com.messenger.prism.repository.AuthRepo;
import com.messenger.prism.service.auth.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private AuthRepo storeUserRepo;
    @Autowired
    private EmailSenderServiceImpl emailSenderService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthenticationProvider authenticationProvider;

    public void deleteSession(HttpServletRequest request) {
        request.getSession(false).invalidate();
    }

    public void sessionAuthentication(HttpServletRequest request, HttpServletResponse response,
                                      String email, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email
                , password);
        Authentication auth = authenticationProvider.authenticate(token);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }

    public UserModel login(UserLoginModel user) throws UserNotFoundException,
            IncorrectPasswordException {
        Auth storedUser = storeUserRepo.findByEmail(user.getEmail());
        boolean isUserNotFound = storedUser == null;
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        boolean isPasswordIncorrect = !encoder.matches(user.getPassword(),
                storedUser.getPassword());
        if (isPasswordIncorrect) {
            throw new IncorrectPasswordException();
        }
        return UserModel.toModel(storedUser);
    }

    public void deleteUser(Authentication authentication, Integer id) throws UserNotFoundException, PermissionsException {
        Optional<Auth> storedUser = storeUserRepo.findById(id);
        boolean isUserNotFound = storeUserRepo.findById(id).isEmpty();
        checkPermission(authentication, storedUser);
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        storeUserRepo.deleteById(id);
    }

    public UserModel getCurrentUser(Authentication authentication) throws UserNotFoundException {
        String currentUserLogin = authentication.getName();
        Auth storedUser = storeUserRepo.findByEmail(currentUserLogin);
        boolean isUserNotFound = storedUser == null;
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        return UserModel.toModel(storedUser);
    }

    public UserModel editUserPassword(Authentication authentication, Integer id,
                                      EditPasswordModel passwords) throws PermissionsException,
            UserNotFoundException, EmptyPasswordException, PasswordIsTooWeakException,
            TooLongPasswordException, TooShortPasswordException, IncorrectPasswordException {
        Optional<Auth> storedUser = storeUserRepo.findById(id);
        boolean isUserNotFound = storedUser.isEmpty();
        checkPermission(authentication, storedUser);
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        boolean isOldPasswordIncorrect = !encoder.matches(passwords.getOldPassword(),
                storedUser.get().getPassword());
        if (isOldPasswordIncorrect) {
            throw new IncorrectPasswordException();
        }
        passwords.setNewPassword(passwords.getNewPassword().trim());
        checkPasswordValidity(passwords.getNewPassword());
        storedUser.get().setPassword(encoder.encode(passwords.getNewPassword()));
        storeUserRepo.save(storedUser.get());
        return UserModel.toModel(storedUser.get());
    }

    public UserModel restoreUserPassword(String code, ActivationCodeModel user,
                                         RestorePasswordModel passwords) throws ActivationCodeExpireException, IncorrectConfirmPasswordException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException {
        ActivationCodeModel storedUser = emailSenderService.getUserByActivationCode(code);
        boolean isPasswordsNotSame =
                !passwords.getConfirmPassword().equals(passwords.getPassword());
        this.checkPasswordValidity(passwords.getPassword());
        if (isPasswordsNotSame) {
            throw new IncorrectConfirmPasswordException();
        }
        storedUser.setPassword(encoder.encode(passwords.getPassword()));
        storeUserRepo.save(ActivationCodeModel.toAuth(storedUser));
        return UserModel.toModel(ActivationCodeModel.toAuth(storedUser));
    }


    public void sendEditUserEmailCode(Authentication authentication, Integer id, String email) throws PermissionsException, UserNotFoundException, UserAlreadyExistException, EmptyEmailException, IncorectEmailException {
        Optional<Auth> storedUser = storeUserRepo.findById(id);
        Auth changedEmailUser = storeUserRepo.findByEmail(email);
        boolean isUserNotFound = storedUser.isEmpty();
        boolean isLoginAlreadyExists = changedEmailUser != null;
        checkPermission(authentication, storedUser);
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        if (isLoginAlreadyExists) {
            throw new UserAlreadyExistException();
        }
        checkEmailValidity(email);
        storedUser.get().setEmail(email);
        emailSenderService.saveActivationCode(storedUser.get(), "Wisit this link to confirm your "
                + "email", "/user/email/confirm?code=");
    }

    public void sendRegitrationCode(UserRegistrationModel user) throws IncorrectConfirmPasswordException, UserAlreadyExistException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, EmptyEmailException, IncorectEmailException {
        boolean isDeveloperFieldMissing = user.getIsDeveloper() == null;
        boolean isUserAlreadyExists = storeUserRepo.findByEmail(user.getEmail()) != null;
        boolean isConfirmPasswordInorrect =
                !(user.getConfirmPassword() != null && user.getConfirmPassword().equals(user.getPassword()));
        if (isDeveloperFieldMissing) {
            user.setIsDeveloper(false);
        }
        if (isUserAlreadyExists) {
            throw new UserAlreadyExistException();
        }
        if (isConfirmPasswordInorrect) {
            throw new IncorrectConfirmPasswordException();
        }
        checkEmailValidity(user.getEmail());
        checkPasswordValidity(user.getPassword());
        Auth userEntity = UserRegistrationModel.toEntity(user, encoder);
        emailSenderService.saveActivationCode(userEntity, "Wisit this link to activate your " +
                "account", "/registration/confirm?code=");
    }

    public void sendRestorePasswordCode(String email) throws UserNotFoundException {
        Auth currentUser = storeUserRepo.findByEmail(email);
        boolean isUserNotFound = currentUser == null;
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        emailSenderService.saveActivationCode(currentUser, "Wisit this link to restore your " +
                "password ", "/user/restore-password/confirm?code=");
    }

    public UserModel saveUserAfterConfirm(ActivationCodeModel user) {
        Auth userEntity = ActivationCodeModel.toAuth(user);
        storeUserRepo.save(userEntity);
        return UserModel.toModel(userEntity);
    }

    private void checkPermission(Authentication authentication, Optional<Auth> storedUser) throws PermissionsException {
        Auth currentUser = storeUserRepo.findByEmail(authentication.getName());
        boolean isCurrentUserNotDeveloper = !currentUser.getRole().equals("DEVELOPER");
        boolean isNotCurrentUserToDelete =
                storedUser.isPresent() && !(Objects.equals(currentUser.getId(),
                        storedUser.get().getId()));
        boolean isUserHaveNotPermission = isCurrentUserNotDeveloper && isNotCurrentUserToDelete;
        if (isUserHaveNotPermission) {
            throw new PermissionsException();
        }
    }

    private void checkPasswordValidity(String password) throws TooLongPasswordException,
            TooShortPasswordException, EmptyPasswordException, PasswordIsTooWeakException {
        boolean isPasswordShort = password.length() < 6;
        boolean isPasswordTooLong = password.length() > 16;
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
        this.isPasswordTooWeak(password);
    }

    private void isPasswordTooWeak(String password) throws PasswordIsTooWeakException {
        boolean isPasswordIncludesCapitalLetters = password.matches(".*[A-ZА-Я].*");
        boolean isPasswordIncluidesLowerLetters = password.matches(".*[a-zа-я].*");
        boolean isPasswordIncluidesNumbers = password.matches(".*[0-9].*");
        boolean isPasswordIncluidesSpecialCharacters = password.matches(".*[!@#$%^&*()].*");
        if (!isPasswordIncludesCapitalLetters) {
            throw new PasswordIsTooWeakException("must include at least one capital letter");
        }
        if (!isPasswordIncluidesLowerLetters) {
            throw new PasswordIsTooWeakException("must include at least one lower letter");
        }
        if (!isPasswordIncluidesNumbers) {
            throw new PasswordIsTooWeakException("must include at least one number");
        }
        if (!isPasswordIncluidesSpecialCharacters) {
            throw new PasswordIsTooWeakException("must include at least one special character");
        }
    }

    private void checkEmailValidity(String email) throws EmptyEmailException,
            IncorectEmailException {
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
