package com.messenger.prism.service;

import com.messenger.prism.entity.Auth;
import com.messenger.prism.exception.PermissionsException;
import com.messenger.prism.exception.auth.IncorrectConfirmPasswordException;
import com.messenger.prism.exception.auth.IncorrectPasswordException;
import com.messenger.prism.exception.auth.UserAlreadyExistException;
import com.messenger.prism.exception.auth.UserNotFoundException;
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

    public void authentication(HttpServletRequest request, HttpServletResponse response, String login, String password) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, password);
        Authentication auth = authenticationProvider.authenticate(token);
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }

    private void isUserHavePermission(Authentication authentication, Optional<Auth> storedUser) throws PermissionsException {
        Auth currentUser = repo.findByLogin(authentication.getName());
        boolean isCurrentUserNotDeveloper = !currentUser.getRole().equals("DEVELOPER");
        boolean isNotCurrentUserToDelete = storedUser.isPresent() && !currentUser.getId().equals(storedUser.get().getId());
        boolean isUserHaveNotPermission = isCurrentUserNotDeveloper && isNotCurrentUserToDelete;
        if (isUserHaveNotPermission) {
            throw new PermissionsException();
        }
    }


    public UserModel regitration(UserRegistrationModel user) throws IncorrectConfirmPasswordException, UserAlreadyExistException {
        boolean isDeveloperFieldMissing = user.getIsDeveloper() == null;
        boolean isUserAlreadyExists = repo.findByLogin(user.getLogin()) != null;
        boolean isConfirmPasswordInorrect = !(user.getConfirmPassword() != null
                && user.getConfirmPassword().equals(user.getPassword()));
        if (isDeveloperFieldMissing) {
            user.setIsDeveloper(false);
        }
        if (isUserAlreadyExists) {
            throw new UserAlreadyExistException();
        }
        if (isConfirmPasswordInorrect) {
            throw new IncorrectConfirmPasswordException();
        }
        Auth userEntity = UserRegistrationModel.toEntity(user, encoder);
        repo.save(userEntity);
        return UserModel.toModel(userEntity);
    }

    public UserModel login(UserLoginModel user) throws UserNotFoundException, IncorrectPasswordException {
        Auth storedUser = repo.findByLogin(user.getLogin());
        boolean isUserNotFound = storedUser == null;
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        boolean isPasswordIncorrect = !encoder.matches(user.getPassword(), storedUser.getPassword());
        if (isPasswordIncorrect) {
            throw new IncorrectPasswordException();
        }
        return UserModel.toModel(storedUser);
    }

    public void deleteUser(Authentication authentication, Integer id) throws UserNotFoundException, PermissionsException {
        Optional<Auth> storedUser = repo.findById(id);
        boolean isUserNotFound = repo.findById(id).isEmpty();
        isUserHavePermission(authentication, storedUser);
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        repo.deleteById(id);
    }

    public UserModel getCurrentUser(Authentication authentication) throws UserNotFoundException {
        String currentUserLogin = authentication.getName();
        Auth storedUser = repo.findByLogin(currentUserLogin);
        boolean isUserNotFound = storedUser == null;
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        return UserModel.toModel(storedUser);
    }

    public UserModel editUserLogin(Authentication authentication, Integer id, Auth login) throws PermissionsException, UserNotFoundException, UserAlreadyExistException {
        Optional<Auth> storedUser = repo.findById(id);
        Auth changedLoginUser = repo.findByLogin(login.getLogin());
        boolean isUserNotFound = storedUser.isEmpty();
        boolean isLoginAlreadyExists = changedLoginUser != null;
        isUserHavePermission(authentication, storedUser);
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        if (isLoginAlreadyExists) {
            throw new UserAlreadyExistException();
        }
        storedUser.get().setLogin(login.getLogin());
        repo.save(storedUser.get());
        return UserModel.toModel(storedUser.get());
    }

    public UserModel editUserPassword(Authentication authentication, Integer id, Auth password) throws PermissionsException, UserNotFoundException {
        Optional<Auth> storedUser = repo.findById(id);
        boolean isUserNotFound = storedUser.isEmpty();
        isUserHavePermission(authentication, storedUser);
        if (isUserNotFound) {
            throw new UserNotFoundException();
        }
        storedUser.get().setPassword(encoder.encode(password.getPassword()));
        repo.save(storedUser.get());
        return UserModel.toModel(storedUser.get());
    }
}
