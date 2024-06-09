package com.messenger.prism.repository;

import com.messenger.prism.entity.Chat;
import org.springframework.data.repository.CrudRepository;

public interface ChatRepo extends CrudRepository<Chat, Integer> {
}
