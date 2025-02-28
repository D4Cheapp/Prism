package com.prism.messenger.model;

import com.prism.messenger.entity.Auth;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("ActivationCode")
public class ActivationCodeModel implements Serializable {

  private Integer id;
  private String email;
  private String password;
  private String role;
  private String code;

  public ActivationCodeModel(Auth auth, String code) {
    this.code = code;
    this.email = auth.getEmail();
    this.password = auth.getPassword();
    this.role = auth.getRole();
    this.id = auth.getId();
  }

  public static Auth toAuth(ActivationCodeModel activationCodeModel) {
    Auth user = new Auth();
    user.setEmail(activationCodeModel.getEmail());
    user.setPassword(activationCodeModel.getPassword());
    user.setRole(activationCodeModel.getRole());
    user.setId(activationCodeModel.getId());
    return user;
  }
}
