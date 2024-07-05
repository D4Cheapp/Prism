package com.prism.messenger.model;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestAttemptModel implements Serializable {

  private String attemptType;
  private Integer attemptCount;
  private long lastAttemptTime;
}
