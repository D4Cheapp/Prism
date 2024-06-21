package com.messenger.prism.entity;

import jakarta.persistence.*;
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
    @NotNull
    @Column(unique = true)
    private String email;
    @NotNull
    @Column(unique = true)
    private String password;
    @NotNull
    @Check(constraints = "role IN ('USER', 'ADMIN')")
    private String role;
}
