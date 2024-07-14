package com.prism.messenger.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.GeneratedValue.UUIDGenerator;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Data
@Node("Chat")
public class Chat {

  @Id
  @GeneratedValue(generatorClass = UUIDGenerator.class)
  private String id;
  @Property("pinnedMessageId")
  private String pinnedMessageId;
}
