package com.prism.messenger.repository;

import com.prism.messenger.entity.Group;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface GroupRepository extends Neo4jRepository<Group, String> {

  @Query("OPTIONAL MATCH (g:Group) WHERE g.id = $id RETURN COUNT(g) = 0")
  Optional<Boolean> findOneById(UUID id);

  @Query("MATCH (p:Profile {tag: $memberTag}) MATCH (g:Group) WHERE g.id = $groupId CREATE (p)<-[:MEMBER]-(g)")
  void addUserToGroup(String memberTag, String groupId);

  @Query("MATCH (p:Profile {email: $email}) CREATE (g:Group) SET g.id = $groupModel SET g.name = $groupName CREATE (p)<-[:ADMIN]-(g)")
  void createGroup(String groupModel, String groupName, String email);
}
