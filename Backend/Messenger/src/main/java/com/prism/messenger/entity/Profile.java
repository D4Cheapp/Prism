package com.prism.messenger.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Node("Profile")
public class Profile {
    @Id
    @GeneratedValue(generatorRef = "usertag")
    private String tag;
    @Property("email")
    private String email;
    @GeneratedValue(generatorRef = "username")
    @Property("name")
    private String name;
    @Property("telephone")
    private String telephone;
    @Property("status")
    private String status;
    @Property("isOnline")
    private boolean isOnline;
    @Property("lastOnlineTime")
    private long lastOnlineTime;
    @Property("profilePicturePath")
    private String profilePicturePath;
    @Relationship(type = "MEMBER", direction = Relationship.Direction.OUTGOING)
    private List<Chat> chatList = new ArrayList<>();
    @Relationship(type = "FRIEND", direction = Relationship.Direction.OUTGOING)
    private List<Profile> friendList = new ArrayList<>();
    @Relationship(type = "BLOCKED", direction = Relationship.Direction.OUTGOING)
    private List<Profile> blockedList = new ArrayList<>();
}
