package com.prism.messenger.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.GeneratedValue.UUIDGenerator;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.schema.Relationship.Direction;

@Data
@Node("Chat")
public class Chat {

  @Id
  @GeneratedValue(generatorClass = UUIDGenerator.class)
  private String id;
  @Property("pinnedMessageId")
  private String pinnedMessageId;
  @Relationship(type = "MEMBER", direction = Direction.INCOMING)
  private List<Profile> memberList = new ArrayList<>();
  @Relationship(type = "MESSAGE", direction = Direction.OUTGOING)
  private List<Message> messageList = new ArrayList<>();
}
