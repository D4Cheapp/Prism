package com.prism.messenger.repository;

import com.prism.messenger.entity.Profile;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface ProfileRepository extends Neo4jRepository<Profile, String> {

  @Query("MATCH (p:Profile {email: $email}) RETURN p")
  Optional<Profile> findByEmail(String email);

  @Query("MATCH (p:Profile {phoneNumber: $phoneNumber}) RETURN p")
  Optional<Profile> findByPhoneNumber(String phoneNumber);

  @Query("MATCH (p:Profile {email: $oldEmail}) SET p.email = $newEmail")
  void changeEmail(String oldEmail, String newEmail);

  @Query("MATCH (p:Profile {tag: $oldTag}) SET p.tag = $newTag")
  void changeTag(String oldTag, String newTag);

  @Query("MATCH (p:Profile {email: $email}) SET p.profilePicturePath = $path")
  void changeProfilePicture(String email, String path);

  @Query("MATCH (p:Profile {email: $email}) SET p.status = $status")
  void changeProfileStatus(String email, String status);

  @Query("MATCH (p:Profile {email: $email}) SET p.phoneNumber = $phoneNumber")
  void changeProfilePhoneNumber(String email, String phoneNumber);

  @Query("MATCH (p:Profile {email: $email}) SET p.name = $name")
  void changeProfileName(String email, String name);
}
