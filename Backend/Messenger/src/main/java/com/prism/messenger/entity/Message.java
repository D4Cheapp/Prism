package com.prism.messenger.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("Message")
public class Message {

  @Id
  @GeneratedValue
  private String tag;
}
