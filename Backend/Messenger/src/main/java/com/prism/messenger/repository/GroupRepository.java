package com.prism.messenger.repository;

import com.prism.messenger.entity.Group;
import com.prism.messenger.entity.Profile;
import com.prism.messenger.model.group.QueryGroupListReceiveModel;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface GroupRepository extends Neo4jRepository<Group, String> {

  @Query("MATCH (g:Group {id: $groupId}) RETURN g")
  Optional<Group> findGroupById(String groupId);

  @Query("OPTIONAL MATCH (g:Group {id: $id}) RETURN COUNT(g) = 0")
  Optional<Boolean> isGroupExist(String id);

  @Query("MATCH (p:Profile {tag: $memberTag}) MATCH (g:Group {id: $groupId}) CREATE (p)<-[:MEMBER]-(g)")
  void addUserToGroup(String memberTag, String groupId);

  @Query("CREATE (g:Group {id: $groupId, name: $groupName})")
  void createGroup(String groupId, String groupName, String email);

  @Query(
      "MATCH (g:Group {id: $groupId})-[:MEMBER {isAdmin: true}]->(p:Profile {email: $email}) RETURN COUNT(p) = 1")
  Optional<Boolean> isGroupAdminByEmail(String email, String groupId);

  @Query("MATCH (g:Group {id: $groupId})-[:MEMBER {isAdmin: true}]->(p:Profile {tag: $profileTag}) RETURN COUNT(p) = 1")
  Optional<Boolean> isGroupAdminByTag(String profileTag, String groupId);

  @Query("MATCH (g:Group {id: $groupId})-[mr:MEMBER]->(p:Profile) DELETE mr DETACH DELETE g")
  void deleteGroup(String groupId);

  @Query(
      "MATCH (p:Profile {tag: $profileTag})<-[mr:MEMBER]-(g:Group {id: $groupId}) SET mr.isAdmin = true RETURN p")
  Optional<Profile> addGroupAdminByTag(String profileTag, String groupId);

  @Query(
      "MATCH (p:Profile {email: $email})<-[mr:MEMBER]-(g:Group {id: $groupId}) SET mr.isAdmin = true")
  void addGroupAdminByEmail(String email, String groupId);

  @Query("MATCH (g:Group {id: $groupId})-[ar:MEMBER {isAdmin: true}]->(p:Profile {tag: $profileTag}) SET ar.isAdmin = null")
  void deleteGroupAdmin(String profileTag, String groupId);

  @Query("MATCH (g:Group {id: $groupId})-[:MEMBER {isAdmin: true}]->(p:Profile) RETURN COUNT(p) = 1")
  Optional<Boolean> isLastGroupAdmin(String groupId);

  @Query("MATCH (g:Group {id: $groupId}) SET g.name = $groupName RETURN g")
  Optional<Group> changeGroupName(String groupId, String groupName);

  @Query("MATCH (g:Group {id: $groupId}) SET g.description = $groupDescription RETURN g")
  Optional<Group> changeGroupDescription(String groupId, String groupDescription);

  @Query("MATCH (g:Group {id: $groupId})-[:MEMBER]->(p:Profile {email: $email}) RETURN COUNT(g) = 1")
  Optional<Boolean> isUserInGroup(String email, String groupId);

  @Query("MATCH (g:Group {id: $groupId})-[mr:MEMBER]->(p:Profile {tag: $profileTag}) DETACH DELETE mr")
  void deleteGroupMember(String groupId, String profileTag);

  @Query("MATCH (g:Group)-[:MEMBER]->(:Profile {email: $email}) RETURN COUNT(g) AS totalCount, COLLECT(g) AS groups SKIP $page LIMIT $size")
  Optional<QueryGroupListReceiveModel> getGroupList(String email, Integer page, Integer size);
}
