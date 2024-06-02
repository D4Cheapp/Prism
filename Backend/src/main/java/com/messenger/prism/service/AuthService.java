package com.messenger.prism.service;

import com.messenger.prism.entity.AuthEntity;
import com.messenger.prism.model.auth.UserLoginModel;
import com.messenger.prism.model.auth.UserModel;
import com.messenger.prism.model.auth.UserRegistrationModel;
import com.messenger.prism.repository.AuthRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AuthRepo repo;

    @Autowired
    private PasswordEncoder encoder;

    public UserModel regitration(UserRegistrationModel user) throws Exception {
        boolean isAdminFieldMissing = user.getIsAdmin() == null;
        boolean isUserAlreadyExists = repo.findByLogin(user.getLogin()) != null;
        boolean isConfirmPasswordInorrect =
                !(user.getConfirmPassword() != null
                        && user.getConfirmPassword()
                        .equals(user.getPassword()));
        if (isAdminFieldMissing) {
            user.setIsAdmin(false);
        }
        if (isUserAlreadyExists) {
            throw new Exception("User with this login already exists");
        }
        if (isConfirmPasswordInorrect) {
            throw new Exception("Incorrect confirm password");
        }
        AuthEntity userEntity = UserRegistrationModel.toEntity(user, encoder);
        repo.save(userEntity);
        return UserModel.toModel(userEntity);
    }

    public UserModel login(UserLoginModel user) throws Exception {
        AuthEntity storedUser = repo.findByLogin(user.getLogin());
        boolean isUserNotFound = storedUser == null;
        if (isUserNotFound) {
            throw new Exception("User not found");
        }
        boolean isPasswordIncorrect = !encoder.matches(user.getPassword(), storedUser.getPassword());
        if (isPasswordIncorrect) {
            throw new Exception("Incorrect password");
        }
        return UserModel.toModel(storedUser);
    }

    public void deleteUser(Integer id) {
        boolean isUserNotFound = repo.findById(id).isEmpty();
        if (isUserNotFound) {
            throw new RuntimeException("User not found");
        }
        repo.deleteById(id);
    }

    public UserModel editUserLogin(Integer id, AuthEntity login) throws Exception {
        Optional<AuthEntity> storedUser = repo.findById(id);
        AuthEntity changedLoginUser = repo.findByLogin(login.getLogin());
        boolean isUserNotFound = storedUser.isEmpty();
        boolean isLoginAlreadyExists = changedLoginUser != null;
        if (isUserNotFound) {
            throw new Exception("User not found");
        }
        if (isLoginAlreadyExists) {
            throw new Exception("User with this login already exists");
        }
        storedUser.get().setLogin(login.getLogin());
        repo.save(storedUser.get());
        return UserModel.toModel(storedUser.get());
    }

    public UserModel editUserPassword(Integer id, AuthEntity password) throws Exception {
        Optional<AuthEntity> storedUser = repo.findById(id);
        boolean isUserNotFound = storedUser.isEmpty();
        if (isUserNotFound) {
            throw new Exception("User not found");
        }
        storedUser.get().setPassword(encoder.encode(password.getPassword()));
        repo.save(storedUser.get());
        return UserModel.toModel(storedUser.get());
    }
}
