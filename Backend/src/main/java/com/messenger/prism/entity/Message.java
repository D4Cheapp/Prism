package com.messenger.prism.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userId;
    private String text;
    private Date receiveTime;
    private boolean isRead;
    private boolean isEdited;
    private int replyId;
    private String voiceMessageUrl;
    private String videoMessageUrl;
    private String[] fileUrl;
    private String[] photoUrl;
}
