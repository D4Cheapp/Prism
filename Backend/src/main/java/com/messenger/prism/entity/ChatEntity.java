package com.messenger.prism.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class ChatEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  //TODO: найти способ хранения сообщений
  private String[] messageId;
  private Boolean isGroup;

  private String pinMessageId;
  private String chatPictureUrl;

  private String firstPersonId;
  private String secondPersonId;

  private String chatName;
  //TODO: найти способ хранения пользователей
  private String[] memberId;
  private String adminId;
}
