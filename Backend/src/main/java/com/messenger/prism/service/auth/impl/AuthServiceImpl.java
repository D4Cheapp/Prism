package com.messenger.prism.service.auth.impl;

import com.messenger.prism.entity.Auth;
import com.messenger.prism.exception.PermissionsException;
import com.messenger.prism.exception.TooManyAttemptsException;
import com.messenger.prism.exception.auth.ActivationCodeExpireException;
import com.messenger.prism.exception.auth.IncorrectConfirmCodeException;
import com.messenger.prism.exception.auth.UserNotFoundException;
import com.messenger.prism.exception.auth.password.*;
import com.messenger.prism.model.auth.*;
import com.messenger.prism.repository.AuthRepo;
import com.messenger.prism.service.auth.AuthService;
import com.messenger.prism.util.AuthUtils;
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

import java.util.Date;
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

    public void deactivateSession(HttpServletRequest request) {
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
        AuthUtils.checkPermission(authentication, storedUser, storeUserRepo);
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

    public UserModel editUserPassword(Authentication authentication, EditPasswordModel passwords) throws PermissionsException, UserNotFoundException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, IncorrectPasswordException {
        Optional<Auth> storedUser = storeUserRepo.findById(passwords.getId());
        boolean isUserNotFound = storedUser.isEmpty();
        AuthUtils.checkPermission(authentication, storedUser, storeUserRepo);
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        boolean isOldPasswordIncorrect = !encoder.matches(passwords.getOldPassword(),
                storedUser.get().getPassword());
        if (isOldPasswordIncorrect) {
            throw new IncorrectPasswordException();
        }
        passwords.setNewPassword(passwords.getNewPassword().trim());
        AuthUtils.checkPasswordValidity(passwords.getNewPassword());
        storedUser.get().setPassword(encoder.encode(passwords.getNewPassword()));
        storeUserRepo.save(storedUser.get());
        return UserModel.toModel(storedUser.get());
    }

    public UserModel restoreUserPassword(String email, RestorePasswordModel passwords) throws ActivationCodeExpireException, IncorrectConfirmPasswordException, EmptyPasswordException, PasswordIsTooWeakException, TooLongPasswordException, TooShortPasswordException, IncorrectConfirmCodeException {
        ActivationCodeModel storedUser = emailSenderService.getUserByEmail(email);
        boolean isPasswordsNotSame =
                !passwords.getConfirmPassword().equals(passwords.getPassword());
        boolean isIncorrectCode = !storedUser.getCode().equals(passwords.getCode());
        AuthUtils.checkPasswordValidity(passwords.getPassword());
        if (isIncorrectCode) {
            throw new IncorrectConfirmCodeException();
        }
        if (isPasswordsNotSame) {
            throw new IncorrectConfirmPasswordException();
        }
        storedUser.setPassword(encoder.encode(passwords.getPassword()));
        storeUserRepo.save(ActivationCodeModel.toAuth(storedUser));
        return UserModel.toModel(ActivationCodeModel.toAuth(storedUser));
    }

    public UserModel saveUserAfterConfirm(ActivationCodeModel user, String code) throws IncorrectConfirmCodeException {
        boolean isActivationCodeIncorrect = !code.equals(user.getCode());
        if (isActivationCodeIncorrect) {
            throw new IncorrectConfirmCodeException();
        }
        Auth userEntity = ActivationCodeModel.toAuth(user);
        storeUserRepo.save(userEntity);
        return UserModel.toModel(userEntity);
    }

    public void checkTrottleRequest(HttpServletRequest request, String type) throws TooManyAttemptsException {
        Object storredAttempts = request.getSession().getAttribute(type + "-attempts");
        boolean isFirstAttempt = storredAttempts == null;
        if (isFirstAttempt) {
            setFirstAttempt(request, type);
        } else {
            checkTrottleRequests(storredAttempts, request, type);
        }
    }

    private void setFirstAttempt(HttpServletRequest request, String type) {
        Date time = new Date();
        RequestAttemptModel newAttempt = new RequestAttemptModel(type, 1, time.getTime());
        request.getSession().setAttribute(type + "-attempts", newAttempt);
    }

    private void checkTrottleRequests(Object storredAttempts, HttpServletRequest request,
                                      String type) throws TooManyAttemptsException {
        RequestAttemptModel attemptModel = (RequestAttemptModel) storredAttempts;
        Date time = new Date();
        int limitAttempts = type.equals("login") || type.equals("registration") ? 5 : 1;
        boolean isLimitAttemt = attemptModel.getAttemptCount() >= limitAttempts;
        boolean isAttemptExpired = attemptModel.getLastAttemptTime() + 120000 < time.getTime();
        if (isLimitAttemt && isAttemptExpired) {
            setFirstAttempt(request, type);
        }
        if (isLimitAttemt && !isAttemptExpired) {
            long remainingMillisecondsTime =
                    attemptModel.getLastAttemptTime() + 120000 - time.getTime();
            long seconds = (remainingMillisecondsTime / 1000) % 60;
            long minutes = (remainingMillisecondsTime / 1000) / 60;
            String remainingStringTime = String.format("%02d:%02d", minutes, seconds);
            throw new TooManyAttemptsException(remainingStringTime);
        }
        if (!isLimitAttemt) {
            attemptModel.setAttemptCount(attemptModel.getAttemptCount() + 1);
            attemptModel.setLastAttemptTime(time.getTime());
            request.getSession().setAttribute(type + "-attempts", attemptModel);
        }
    }
}
