package com.messenger.prism.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //TODO: найти способ хранения сообщений
    private int[] messageId;
    private boolean isGroup;
    private String pinMessageId;
    private String chatPictureUrl;
    private String firstPersonId;
    private String secondPersonId;
    private String chatName;
    //TODO: найти способ хранения пользователей
    private int[] memberId;
    private String adminId;
}
