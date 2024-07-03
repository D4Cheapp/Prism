package com.prism.messenger.repository;

import com.prism.messenger.entity.Auth;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.repository.CrudRepository;

@RedisHash
public interface AuthRepo extends CrudRepository<Auth, Integer> {
    Auth findByEmail(String email);
}
