package com.messenger.prism.repository;

import com.messenger.prism.entity.Auth;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;

@RedisHash
public interface AuthRepo extends CrudRepository<Auth, Integer> {
    Auth findByLogin(String login);
}
