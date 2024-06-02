package com.messenger.prism.repository;

import com.messenger.prism.entity.ChatEntity;
import org.springframework.data.repository.CrudRepository;

public interface ChatRepo extends CrudRepository<ChatEntity, Integer> {
}
