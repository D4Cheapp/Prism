package com.prism.messenger.repository;

import com.prism.messenger.entity.Group;
import com.prism.messenger.entity.Profile;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface GroupRepository extends Neo4jRepository<Group, String> {

  @Query("MATCH (g:Group) WHERE g.id = $groupId RETURN g")
  Optional<Group> findGroupById(String groupId);

  @Query("OPTIONAL MATCH (g:Group) WHERE g.id = $id RETURN COUNT(g) = 0")
  Optional<Boolean> findOneById(UUID id);

  @Query("MATCH (p:Profile {tag: $memberTag}) MATCH (g:Group) WHERE g.id = $groupId CREATE (p)<-[:MEMBER]-(g)")
  void addUserToGroup(String memberTag, String groupId);

  @Query("MATCH (p:Profile {email: $email}) CREATE (g:Group) SET g.id = $groupId SET g.name = "
      + "$groupName CREATE (p)<-[:ADMIN]-(g)")
  void createGroup(String groupId, String groupName, String email);

  @Query("MATCH (g:Group)-[:ADMIN]->(p:Profile {email: $email}) WHERE g.id = $groupId RETURN COUNT(p) = 1")
  Optional<Boolean> isGroupAdminByEmail(String email, String groupId);

  @Query("MATCH (g:Group)-[:ADMIN]->(p:Profile {tag: $profileTag}) WHERE g.id = $groupId RETURN COUNT(p) = 1")
  Optional<Boolean> isGroupAdminByTag(String profileTag, String dialogId);

  @Query(
      "MATCH (g:Group)-[mr:MEMBER]->(p:Profile) WHERE g.id = $groupId OPTIONAL MATCH (g)-[messageR:MESSAGE]->"
          + "(m:Message) MATCH (g)-[ar:ADMIN]->(p:Profile) DELETE mr, messageR, ar DETACH DELETE m, g")
  void deleteGroup(String groupId);

  @Query("MATCH (p:Profile {tag: $profileTag}) MATCH (g:Group) WHERE g.id = $groupId CREATE (p)<-[:ADMIN]-(g) RETURN p")
  Optional<Profile> addGroupAdmin(String profileTag, String groupId);

  @Query("MATCH (g:Group)-[ar:ADMIN]->(p:Profile {tag: $profileTag}) WHERE g.id = $groupId DELETE ar")
  void deleteGroupAdmin(String profileTag, String groupId);

  @Query("MATCH (g:Group)-[:ADMIN]->(p:Profile) WHERE g.id = $groupId RETURN COUNT(p) = 1")
  Optional<Boolean> isLastGroupAdmin(String groupId);

  @Query("MATCH (g:Group) WHERE g.id = $groupId SET g.name = $groupName RETURN g")
  Optional<Group> changeGroupName(String groupId, String groupName);

  @Query("MATCH (g:Group) WHERE g.id = $groupId SET g.description = $groupDescription RETURN g")
  Optional<Group> changeGroupDescription(String groupId, String groupDescription);
}
