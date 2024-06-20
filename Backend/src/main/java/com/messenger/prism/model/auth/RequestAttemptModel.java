package com.messenger.prism.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Component
@Scope(value = "session")
public class RequestAttemptModel implements Serializable {
    private String attemptType;
    private Integer attemptCount;
    private long lastAttemptTime;
}
