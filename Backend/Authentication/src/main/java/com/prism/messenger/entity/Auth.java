package com.prism.messenger.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.Check;

@Entity
@Data
@Table(name = "auth", indexes = @Index(name = "email_index", columnList = "email"))
public class Auth {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(unique = true, nullable = false)
  private String email;
  @NotNull
  @Column(unique = true, nullable = false)
  private String password;
  @NotNull
  @Check(constraints = "role IN ('USER', 'DEVELOPER')")
  @Column(nullable = false)
  private String role;
}
