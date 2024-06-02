package com.messenger.prism.repository;

import com.messenger.prism.entity.AuthEntity;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;

@RedisHash
public interface AuthRepo extends CrudRepository<AuthEntity, Integer> {
    AuthEntity findByLogin(String login);
}
