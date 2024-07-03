package com.prism.messenger.repository;

import com.prism.messenger.entity.Profile;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Optional;

public interface ProfileRepository extends Neo4jRepository<Profile, String> {
    @Query("MATCH (a:Profile {email: $email}) RETURN a")
    Optional<Profile> findByEmail(String email);
}
