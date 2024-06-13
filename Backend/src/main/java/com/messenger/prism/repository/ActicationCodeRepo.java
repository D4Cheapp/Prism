package com.messenger.prism.repository;

import com.messenger.prism.exception.auth.ActivationCodeExpireException;
import com.messenger.prism.model.auth.ActivationCodeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class ActicationCodeRepo {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveCode(ActivationCodeModel activationCodeModel) {
        redisTemplate.opsForHash().put(activationCodeModel.getCode(), "id",
                activationCodeModel.getId());
        redisTemplate.opsForHash().put(activationCodeModel.getCode(), "email",
                activationCodeModel.getEmail());
        redisTemplate.opsForHash().put(activationCodeModel.getCode(), "password",
                activationCodeModel.getPassword());
        redisTemplate.opsForHash().put(activationCodeModel.getCode(), "role",
                activationCodeModel.getRole());
        redisTemplate.expire(activationCodeModel.getCode(), 30, TimeUnit.MINUTES);
    }

    public ActivationCodeModel findByActivationCode(String code) throws ActivationCodeExpireException {
        boolean isActivationCodeExist = !Boolean.TRUE.equals(redisTemplate.hasKey(code));
        if (isActivationCodeExist) {
            throw new ActivationCodeExpireException();
        }
        Integer id = (Integer) redisTemplate.opsForHash().get(code, "id");
        String email = (String) redisTemplate.opsForHash().get(code, "email");
        String password = (String) redisTemplate.opsForHash().get(code, "password");
        String role = (String) redisTemplate.opsForHash().get(code, "role");
        return new ActivationCodeModel(id, email, password, role, code);
    }

    public void deleteCode(String code) {
        redisTemplate.delete(code);
    }
}
