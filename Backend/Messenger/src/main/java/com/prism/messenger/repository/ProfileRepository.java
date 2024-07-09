package com.prism.messenger.repository;

import com.prism.messenger.entity.Profile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface ProfileRepository extends Neo4jRepository<Profile, String> {

  @Query("MATCH (p:Profile {email: $email}) RETURN p")
  Optional<Profile> findByEmail(String email);

  @Query("MATCH (p:Profile {tag: $tag}) RETURN p")
  Optional<Profile> findByTag(String tag);

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

  @Query("MATCH (p:Profile {email: $email}) MATCH (f:Profile {tag: $userTag}) WHERE NOT (p)-[:BLOCK]->(f) CREATE (p)-[:BLOCK]->(f)")
  void blockUser(String email, String userTag);

  @Query("MATCH (p:Profile {email: $email})-[r:BLOCK]->(f:Profile {tag: $userTag}) DELETE r")
  void unBlockUser(String email, String userTag);

  @Query("MATCH (p:Profile {email: $email}) MATCH (f:Profile {tag: $friendTag}) WHERE NOT (p)-[:FRIEND]->(f) CREATE (p)-[:FRIEND]->(f)")
  void addFriend(String email, String friendTag);

  @Query("MATCH (p:Profile {email: $email})-[r:FRIEND]->(f:Profile {tag: $friendTag}) DELETE r")
  void deleteFriend(String email, String friendTag);

  @Query("MATCH (p:Profile {tag: $tag}) DETACH DELETE p")
  void deleteByTag(String tag);

  @Query("MATCH (p:Profile {email: $email})-[:FRIEND]->(f:Profile) WHERE (p)<-[:FRIEND]-(f) RETURN COUNT(f)")
  Optional<Integer> getFriendsCount(String email);

  @Query("MATCH (p:Profile {email: $email})<-[:FRIEND]-(f:Profile) WHERE NOT (p)-[:FRIEND]->(f) RETURN COUNT(f)")
  Optional<Integer> getFriendRequestsCount(String email);

  @Query("MATCH (p:Profile {email: $email})-[r:FRIEND]->(f:Profile) WHERE NOT (p)<-[:FRIEND]-(f)  RETURN COUNT(f)")
  Optional<Integer> getSendedFriendRequestCount(String email);

  @Query("MATCH (p:Profile {email: $email})-[r:BLOCK]->(f:Profile) RETURN COUNT(f)")
  Optional<Integer> getBlockListCount(String email);

  @Query("MATCH (p:Profile) WHERE p.tag CONTAINS $tag RETURN COUNT(p)")
  Optional<Integer> getSearchProfileByTagCount(String tag);

  @Query("MATCH (p:Profile {email: $email})-[:FRIEND]->(f:Profile) WHERE (p)<-[:FRIEND]-(f)  RETURN f SKIP $skip LIMIT $limit ")
  Optional<List<Profile>> getFriendList(String email, Integer skip, Integer limit);

  @Query("MATCH (p:Profile {email: $email})<-[:FRIEND]-(f:Profile) WHERE NOT (p)-[:FRIEND]->(f) RETURN f SKIP $skip LIMIT $limit")
  Optional<List<Profile>> getFriendRequestsList(String email, Integer skip, Integer limit);

  @Query("MATCH (p:Profile {email: $email})-[r:FRIEND]->(f:Profile) WHERE NOT (p)<-[:FRIEND]-(f)  RETURN f SKIP $skip LIMIT $limit")
  Optional<List<Profile>> getSendedFriendRequest(String email, Integer skip, Integer limit);

  @Query("MATCH (p:Profile {email: $email})-[r:BLOCK]->(f:Profile) RETURN f SKIP $skip LIMIT $limit")
  Optional<List<Profile>> getBlockList(String email, Integer skip, Integer limit);

  @Query("MATCH (p:Profile) WHERE p.tag CONTAINS $tag RETURN p SKIP $skip LIMIT $limit")
  Optional<List<Profile>> searchProfileByTag(String tag, Integer skip, Integer limit);

  @Query("MATCH (p:Profile {email: $email})-[r]->(f:Profile {tag: $userTag}) RETURN TYPE(r)")
  Optional<String> getRelationToUser(String email, String userTag);

  @Query("MATCH (p:Profile {email: $email})<-[r]-(f:Profile {tag: $userTag}) RETURN TYPE(r)")
  Optional<String> getRelationFromUser(String email, String userTag);

  @Query("MATCH (p:Profile {email: $email})<-[r]-(f:Profile {tag: $userTag}) WHERE NOT (p)-[:FRIEND]->(f) DELETE r")
  void declineFriendRequest(String email, String userTag);

  @Query("MATCH (p:Profile {email: $email}) SET p.isOnline = $isOnline")
  void setIsOnlineStatus(String email, boolean isOnline);

  @Query("MATCH (p:Profile {email: $email}) SET p.lastOnlineTime = $lastOnlineTime")
  void setLastOnlineTime(String email, long lastOnlineTime);
}
