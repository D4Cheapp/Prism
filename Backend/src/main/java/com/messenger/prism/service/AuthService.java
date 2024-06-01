package com.messenger.prism.service;

import com.messenger.prism.entity.AuthEntity;
import com.messenger.prism.model.auth.UserLoginModel;
import com.messenger.prism.model.auth.UserModel;
import com.messenger.prism.model.auth.UserRegistrationModel;
import com.messenger.prism.repository.AuthRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AuthRepo repo;

    public UserModel regitration(UserRegistrationModel user) throws Exception {
        if (user.getIsAdmin() == null) {
            user.setIsAdmin(false);
        }
        if (repo.findByLogin(user.getLogin()) != null) {
            throw new Exception("User with this login already exists");
        }
        if (user.getConfirmPassword() == null || !Objects.equals(user.getConfirmPassword(), user.getPassword())) {
            throw new Exception("Incorrect confirm password");
        }
        AuthEntity userEntity = UserRegistrationModel.toEntity(user);
        repo.save(userEntity);
        return UserModel.toModel(userEntity);
    }

    public UserModel login(UserLoginModel user) throws Exception {
        Optional<AuthEntity> storedUser = Optional.ofNullable(repo.findByLogin(user.getLogin()));
        if (storedUser.isEmpty()) {
            throw new Exception("Incorrect login");
        }
        if (!Objects.equals(storedUser.get().getPassword(), user.getPassword())) {
            throw new Exception("Incorrect password");
        }
        return UserModel.toModel(storedUser.get());
    }

    public void deleteUser(Long id) {
        if (repo.findById(String.valueOf(id)).isEmpty()) {
            throw new RuntimeException("User not found");
        }
        repo.deleteById(String.valueOf(id));
    }

    public UserModel editUserLogin(Long id, AuthEntity login) throws Exception {
        Optional<AuthEntity> storedUser = repo.findById(String.valueOf(id));
        Optional<AuthEntity> changedLoginUser = Optional.ofNullable(repo.findByLogin(login.getLogin()));
        if (storedUser.isEmpty()) {
            throw new Exception("User not found");
        }
        if (changedLoginUser.isPresent()) {
            throw new Exception("User with this login already exists");
        }
        storedUser.get().setLogin(login.getLogin());
        repo.save(storedUser.get());
        return UserModel.toModel(storedUser.get());
    }

    public UserModel editUserPassword(Long id, AuthEntity password) throws Exception {
        Optional<AuthEntity> storedUser = repo.findById(String.valueOf(id));
        if (storedUser.isEmpty()) {
            throw new Exception("User not found");
        }
        storedUser.get().setPassword(password.getPassword());
        repo.save(storedUser.get());
        return UserModel.toModel(storedUser.get());
    }
}
