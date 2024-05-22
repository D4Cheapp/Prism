package com.messenger.prism.repository;

import com.messenger.prism.entity.AuthEntity;
import org.springframework.data.repository.CrudRepository;

public interface AuthRepo extends CrudRepository<AuthEntity, String> {
    AuthEntity findByLogin(String login);
}
