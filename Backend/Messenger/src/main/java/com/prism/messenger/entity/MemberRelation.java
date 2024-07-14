package com.prism.messenger.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@Data
@RelationshipProperties
public class MemberRelation {

  @Id
  @GeneratedValue
  private Long graphId;
  private boolean isAdmin;
  @TargetNode
  private Chat chat;
}
