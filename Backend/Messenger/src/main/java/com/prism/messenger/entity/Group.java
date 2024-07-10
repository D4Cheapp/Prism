package com.prism.messenger.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

@Data
@EqualsAndHashCode(callSuper = true)
@Node("Group")
public class Group extends Chat {

  @Property("name")
  private String name;
  @Property("picturePath")
  private String picturePath;
  @Relationship(type = "ADMIN", direction = Relationship.Direction.OUTGOING)
  private List<Profile> adminList = new ArrayList<>();
}
