package com.messenger.prism.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private Boolean isOnline;
  private String[] chatIDs = {};
  private String profilePicture;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getOnline() {
    return isOnline;
  }

  public void setOnline(Boolean online) {
    isOnline = online;
  }

  public String[] getChatIDs() {
    return chatIDs;
  }

  public void setChatIDs(String[] chatIDs) {
    this.chatIDs = chatIDs;
  }

  public String getProfilePicture() {
    return profilePicture;
  }

  public void setProfilePicture(String profilePicture) {
    this.profilePicture = profilePicture;
  }
}
