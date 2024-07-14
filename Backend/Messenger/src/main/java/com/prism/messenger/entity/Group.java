package com.prism.messenger.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Data
@EqualsAndHashCode(callSuper = true)
@Node("Group")
public class Group extends Chat {

  @Property("name")
  private String name;
  @Property("description")
  private String description;
  @Property("picturePath")
  private String picturePath;
}
