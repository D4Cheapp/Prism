package com.prism.messenger.model;

import lombok.Data;

@Data
public class RabbitMQMessageModel {
    private Object message;
    private String action;
}
