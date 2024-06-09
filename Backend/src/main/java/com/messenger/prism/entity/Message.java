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
  private Integer id;
  private String userId;
  private String text;
  private Date receiveTime;
  private Boolean isRead;
  private Boolean isEdited;
  private String replyId;
  private String voiceMessageUrl;
  private String videoMessageUrl;
  private String[] fileUrl;
  private String[] photoUrl;
}
