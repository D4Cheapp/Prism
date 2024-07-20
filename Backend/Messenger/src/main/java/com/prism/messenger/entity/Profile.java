package com.prism.messenger.entity;

import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@Node("Profile")
public class Profile {

  @Id
  private String tag;
  @NotNull
  @Property("email")
  private String email;
  @Property("name")
  private String name;
  @Property("phoneNumber")
  private String phoneNumber;
  @Property("status")
  private String status;
  @Property("isOnline")
  private boolean isOnline;
  @Property("lastOnlineTime")
  private long lastOnlineTime;
  @Property("profilePicturePath")
  private String profilePicturePath;
  @Relationship(type = "MEMBER")
  private List<Chat> chatList = new ArrayList<>();
  @Relationship(type = "FRIEND")
  private List<Profile> friendList = new ArrayList<>();
  @Relationship(type = "BLOCK")
  private List<Profile> blockedList = new ArrayList<>();
}
