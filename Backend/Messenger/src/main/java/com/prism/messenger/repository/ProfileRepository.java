package com.prism.messenger.repository;

import com.prism.messenger.entity.Profile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

public interface ProfileRepository extends Neo4jRepository<Profile, String> {
    @Query("match (p:Profile) where p.email = $email return p")
    Profile findByEmail(@Param("email") String email);
}
