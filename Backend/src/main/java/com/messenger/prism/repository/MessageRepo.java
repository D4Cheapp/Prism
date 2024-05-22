package com.messenger.prism.repository;

import com.messenger.prism.entity.MessageEntity;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<MessageEntity, Long> {
}
