package com.prism.messenger.repository;

import com.prism.messenger.exception.ActivationCodeExpireException;
import com.prism.messenger.model.ActivationCodeModel;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ActivationCodeRepo {

  @Autowired
  private RedisTemplate<String, Object> redisTemplate;

  public void saveActivationCode(ActivationCodeModel activationCodeModel) {
    redisTemplate.opsForHash()
        .put(activationCodeModel.getEmail(), "code", activationCodeModel.getCode());
    redisTemplate.opsForHash()
        .put(activationCodeModel.getEmail(), "id", activationCodeModel.getId());
    redisTemplate.opsForHash()
        .put(activationCodeModel.getEmail(), "email", activationCodeModel.getEmail());
    redisTemplate.opsForHash()
        .put(activationCodeModel.getEmail(), "password", activationCodeModel.getPassword());
    redisTemplate.opsForHash()
        .put(activationCodeModel.getEmail(), "role", activationCodeModel.getRole());
    redisTemplate.expire(activationCodeModel.getEmail(), 30, TimeUnit.MINUTES);
  }

  public ActivationCodeModel findActivationCodeByEmail(String email)
      throws ActivationCodeExpireException {
    boolean isActivationCodeExist = !Boolean.TRUE.equals(redisTemplate.hasKey(email));
    if (isActivationCodeExist) {
      throw new ActivationCodeExpireException();
    }
    Integer id = (Integer) redisTemplate.opsForHash().get(email, "id");
    String password = (String) redisTemplate.opsForHash().get(email, "password");
    String role = (String) redisTemplate.opsForHash().get(email, "role");
    String code = (String) redisTemplate.opsForHash().get(email, "code");
    return new ActivationCodeModel(id, email, password, role, code);
  }

  public void deleteActivationCode(String email) {
    redisTemplate.delete(email);
  }
}
