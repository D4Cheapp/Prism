package com.messenger.prism.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ChatEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String[] messageID;
  private Boolean isGroup;

  private String pinMessageID;
  private String chatPicture;

  private String firstPersonID;
  private String secondPersonID;

  private String chatName;
  private String[] memberID;
  private String adminID;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String[] getMessageID() {
    return messageID;
  }

  public void setMessageID(String[] messageID) {
    this.messageID = messageID;
  }

  public Boolean getGroup() {
    return isGroup;
  }

  public void setGroup(Boolean group) {
    isGroup = group;
  }

  public String getPinMessageID() {
    return pinMessageID;
  }

  public void setPinMessageID(String pinMessageID) {
    this.pinMessageID = pinMessageID;
  }

  public String getChatPicture() {
    return chatPicture;
  }

  public void setChatPicture(String chatPicture) {
    this.chatPicture = chatPicture;
  }

  public String getFirstPersonID() {
    return firstPersonID;
  }

  public void setFirstPersonID(String firstPersonID) {
    this.firstPersonID = firstPersonID;
  }

  public String getSecondPersonID() {
    return secondPersonID;
  }

  public void setSecondPersonID(String secondPersonID) {
    this.secondPersonID = secondPersonID;
  }

  public String getChatName() {
    return chatName;
  }

  public void setChatName(String chatName) {
    this.chatName = chatName;
  }

  public String[] getMemberID() {
    return memberID;
  }

  public void setMemberID(String[] memberID) {
    this.memberID = memberID;
  }

  public String getAdminID() {
    return adminID;
  }

  public void setAdminID(String adminID) {
    this.adminID = adminID;
  }
}
