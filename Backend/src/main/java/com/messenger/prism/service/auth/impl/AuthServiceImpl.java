package com.messenger.prism.service.auth.impl;

import com.messenger.prism.entity.Auth;
import com.messenger.prism.exception.PermissionsException;
import com.messenger.prism.exception.auth.*;
import com.messenger.prism.model.auth.ActivationCodeModel;
import com.messenger.prism.model.auth.UserLoginModel;
import com.messenger.prism.model.auth.UserModel;
import com.messenger.prism.model.auth.UserRegistrationModel;
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

    public void regitration(UserRegistrationModel user) throws IncorrectConfirmPasswordException,
            UserAlreadyExistException, EmptyPasswordException, PasswordIsTooWeakException,
            TooLongPasswordException, TooShortPasswordException, EmptyEmailException,
            IncorectEmailException {
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
        emailSenderService.saveActivationCode(userEntity, "Please activate your account", "/auth" +
                "/registration/confirm/");
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

    public void editUserEmail(Authentication authentication, Integer id, String email) throws PermissionsException, UserNotFoundException, UserAlreadyExistException, EmptyEmailException, IncorectEmailException {
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
        emailSenderService.saveActivationCode(storedUser.get(), "Please confirm your email",
                "/auth/user/email/confirm/");
    }

    public UserModel editUserPassword(Authentication authentication, Integer id, String password) throws PermissionsException, UserNotFoundException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException {
        Optional<Auth> storedUser = storeUserRepo.findById(id);
        boolean isUserNotFound = storedUser.isEmpty();
        checkPermission(authentication, storedUser);
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        checkPasswordValidity(password);
        storedUser.get().setPassword(encoder.encode(password));
        storeUserRepo.save(storedUser.get());
        return UserModel.toModel(storedUser.get());
    }

    public UserModel saveUserAfterConfirm(ActivationCodeModel user) {
        Auth userEntity = ActivationCodeModel.toAuth(user);
        storeUserRepo.save(userEntity);
        return UserModel.toModel(userEntity);
    }

    public void restorePasswordByEmail(String email) {

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
        boolean isPasswordIncludesCapitalLetters = password.matches(".*[A-ZА-Я].*");
        boolean isPasswordIncluidesLowerLetters = password.matches(".*[a-zа-я].*");
        boolean isPasswordIncluidesNumbers = password.matches(".*[0-9].*");
        boolean isPasswordTooWeak =
                !isPasswordIncluidesNumbers || !isPasswordIncluidesLowerLetters || !isPasswordIncludesCapitalLetters;
        if (isPasswordEmpty) {
            throw new EmptyPasswordException();
        }
        if (isPasswordShort) {
            throw new TooShortPasswordException();
        }
        if (isPasswordTooLong) {
            throw new TooLongPasswordException();
        }
        if (isPasswordTooWeak) {
            throw new PasswordIsTooWeakException();
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
