package com.messenger.prism.repository;

import com.messenger.prism.entity.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<Message, Integer > {
}
