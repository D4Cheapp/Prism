package com.messenger.prism.service;

import com.messenger.prism.entity.Auth;
import com.messenger.prism.exception.PermissionsException;
import com.messenger.prism.exception.auth.*;
import com.messenger.prism.model.auth.UserLoginModel;
import com.messenger.prism.model.auth.UserModel;
import com.messenger.prism.model.auth.UserRegistrationModel;
import com.messenger.prism.repository.AuthRepo;
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

import java.util.Optional;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Service
public class AuthService {
    @Autowired
    private AuthRepo repo;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthenticationProvider authenticationProvider;

    public void authentication(HttpServletRequest request,
                               HttpServletResponse response, String email,
                               String password) {
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication auth = authenticationProvider.authenticate(token);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }

    public UserModel regitration(UserRegistrationModel user) throws IncorrectConfirmPasswordException,
            UserAlreadyExistException, EmptyPasswordException,
            PasswordIsTooWeakException, TooLongPasswordException,
            TooShortPasswordException, EmptyEmailException,
            IncorectEmailException {
        boolean isDeveloperFieldMissing = user.getIsDeveloper() == null;
        boolean isUserAlreadyExists = repo.findByEmail(user.getEmail()) != null;
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
        repo.save(userEntity);
        return UserModel.toModel(userEntity);
    }

    public UserModel login(UserLoginModel user) throws UserNotFoundException,
            IncorrectPasswordException {
        Auth storedUser = repo.findByEmail(user.getEmail());
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

    public void deleteUser(Authentication authentication, Integer id) throws UserNotFoundException,
            PermissionsException {
        Optional<Auth> storedUser = repo.findById(id);
        boolean isUserNotFound = repo.findById(id).isEmpty();
        checkPermission(authentication, storedUser);
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        repo.deleteById(id);
    }

    public UserModel getCurrentUser(Authentication authentication) throws UserNotFoundException {
        String currentUserLogin = authentication.getName();
        Auth storedUser = repo.findByEmail(currentUserLogin);
        boolean isUserNotFound = storedUser == null;
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        return UserModel.toModel(storedUser);
    }

    public UserModel editUserEmail(Authentication authentication, Integer id,
                                   Auth email) throws PermissionsException,
            UserNotFoundException, UserAlreadyExistException,
            EmptyEmailException, IncorectEmailException {
        Optional<Auth> storedUser = repo.findById(id);
        Auth changedLoginUser = repo.findByEmail(email.getEmail());
        boolean isUserNotFound = storedUser.isEmpty();
        boolean isLoginAlreadyExists = changedLoginUser != null;
        checkPermission(authentication, storedUser);
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        if (isLoginAlreadyExists) {
            throw new UserAlreadyExistException();
        }
        checkEmailValidity(email.getEmail());
        storedUser.get().setEmail(email.getEmail());
        repo.save(storedUser.get());
        return UserModel.toModel(storedUser.get());
    }

    public UserModel editUserPassword(Authentication authentication,
                                      Integer id, Auth password) throws PermissionsException,
            UserNotFoundException, EmptyPasswordException,
            PasswordIsTooWeakException, TooLongPasswordException,
            TooShortPasswordException {
        Optional<Auth> storedUser = repo.findById(id);
        boolean isUserNotFound = storedUser.isEmpty();
        checkPermission(authentication, storedUser);
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        checkPasswordValidity(password.getPassword());
        storedUser.get().setPassword(encoder.encode(password.getPassword()));
        repo.save(storedUser.get());
        return UserModel.toModel(storedUser.get());
    }

    private void checkPermission(Authentication authentication,
                                 Optional<Auth> storedUser) throws PermissionsException {
        Auth currentUser = repo.findByEmail(authentication.getName());
        boolean isCurrentUserNotDeveloper = !currentUser.getRole().equals(
                "DEVELOPER");
        boolean isNotCurrentUserToDelete =
                storedUser.isPresent() && !(currentUser.getId() == storedUser.get().getId());
        boolean isUserHaveNotPermission =
                isCurrentUserNotDeveloper && isNotCurrentUserToDelete;
        if (isUserHaveNotPermission) {
            throw new PermissionsException();
        }
    }

    private void checkPasswordValidity(String password) throws TooLongPasswordException,
            TooShortPasswordException, EmptyPasswordException,
            PasswordIsTooWeakException {
        boolean isPasswordShort = password.length() < 6;
        boolean isPasswordTooLong = password.length() > 16;
        boolean isPasswordEmpty = password.isEmpty();
        boolean isPasswordIncludesCapitalLetters =
                password.matches(".*[A-ZА-Я].*");
        boolean isPasswordIncluidesLowerLetters =
                password.matches(".*[a-zа-я].*");
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
