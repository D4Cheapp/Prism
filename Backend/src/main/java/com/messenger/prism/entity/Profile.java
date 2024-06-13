package com.messenger.prism.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String phone;
    private String tag;
    private String status;
    private boolean isOnline;
    private Integer[] chatId;
    private Integer[] friendId;
    private Integer[] blackListId;
    private String profilePictureUrl;
}
