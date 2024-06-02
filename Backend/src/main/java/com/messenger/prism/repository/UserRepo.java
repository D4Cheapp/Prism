package com.messenger.prism.repository;

import com.messenger.prism.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<UserEntity, Integer> {
}
