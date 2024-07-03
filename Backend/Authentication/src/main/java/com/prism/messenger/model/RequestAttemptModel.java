package com.prism.messenger.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RequestAttemptModel implements Serializable {
    private String attemptType;
    private Integer attemptCount;
    private long lastAttemptTime;
}
