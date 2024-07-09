package com.prism.messenger.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node("Chat")
public class Chat {

  @Id
  @GeneratedValue(generatorRef = "chattag")
  private String tag;
}
