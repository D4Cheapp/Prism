package com.messenger.prism.repository;

import com.messenger.prism.entity.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepo extends CrudRepository<Profile, Integer> {
}
