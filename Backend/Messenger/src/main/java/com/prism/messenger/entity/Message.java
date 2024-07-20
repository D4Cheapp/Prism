package com.prism.messenger.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.neo4j.core.schema.Id;

@Data
@Document("Messages")
public class Message {

  @Id
  private String id;
  private String senderId;
  private String text;
  private long sendTime;
  private boolean isRead;
  private boolean isEdited;
  private String forwardMessageId;
  private String voiceMessagePath;
  private String videoMessagePath;
  private String[] filesListPaths;
  private String[] photoListPaths;
}
