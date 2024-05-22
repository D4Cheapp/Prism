package com.messenger.prism.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class MessageEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String userID;
  private String text;
  private Date receiveTime;
  private Boolean isRead;
  private Boolean isEdited;

  private String replyID;
  private String voiceMessage;
  private String videoMessage;
  private String[] file;
  private String[] photo;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Date getReceiveTime() {
    return receiveTime;
  }

  public void setReceiveTime(Date receiveTime) {
    this.receiveTime = receiveTime;
  }

  public Boolean getRead() {
    return isRead;
  }

  public void setRead(Boolean read) {
    isRead = read;
  }

  public Boolean getEdited() {
    return isEdited;
  }

  public void setEdited(Boolean edited) {
    isEdited = edited;
  }

  public String getReplyID() {
    return replyID;
  }

  public void setReplyID(String replyID) {
    this.replyID = replyID;
  }

  public String getVoiceMessage() {
    return voiceMessage;
  }

  public void setVoiceMessage(String voiceMessage) {
    this.voiceMessage = voiceMessage;
  }

  public String getVideoMessage() {
    return videoMessage;
  }

  public void setVideoMessage(String videoMessage) {
    this.videoMessage = videoMessage;
  }

  public String[] getFile() {
    return file;
  }

  public void setFile(String[] file) {
    this.file = file;
  }

  public String[] getPhoto() {
    return photo;
  }

  public void setPhoto(String[] photo) {
    this.photo = photo;
  }
}
