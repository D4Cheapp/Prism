package com.prism.messenger.repository;

import com.prism.messenger.entity.Chat;
import com.prism.messenger.model.chat.QueryChatListReceiveModel;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

public interface ChatRepository extends Neo4jRepository<Chat, String> {

  @Query("OPTIONAL MATCH (c:Chat)-[:MEMBER]->(p:Profile), (c:Chat)-[:MEMBER]->(f:Profile) WHERE p.email = $email AND f.tag = $interlocutorTag  RETURN COUNT(c) = 1")
  Optional<Boolean> checkIsChatExist(String email, String interlocutorTag);

  @Query("OPTIONAL MATCH (c:Chat {id: $chatId}) RETURN COUNT(c) = 0")
  Optional<Boolean> checkIsIdUnique(String chatId);

  @Query("MATCH (p:Profile {email: $email}) MATCH (f:Profile {tag: $interlocutorTag}) CREATE (c:Chat {id: $chatId}) CREATE (p)<-[:MEMBER]-(c) CREATE (c)-[:MEMBER]->(f) RETURN c")
  Optional<Chat> createChat(String chatId, String email, String interlocutorTag);

  @Query("MATCH (c:Chat)-[:MEMBER]->(p:Profile {email: $email}) WHERE c.id = $dialogId RETURN COUNT(p) = 1")
  Optional<Boolean> isUserInChat(String email, String dialogId);

  @Query("MATCH (c:Chat {id: $dialogId})-[mr:MEMBER]->(:Profile) DELETE mr DETACH DELETE c")
  void deleteChat(String dialogId);

  @Query("MATCH (c:Chat)-[:MEMBER]->(:Profile {email: $email}) RETURN COUNT(c) as totalCount, "
      + "COLLECT(c) AS chats SKIP $page LIMIT $size")
  Optional<QueryChatListReceiveModel> getChatList(String email, Integer page, Integer size);

  @Query("MATCH (c:Chat {id: $chatId})-[:MEMBER]->(p:Profile) WHERE p.email <> $email RETURN p.tag "
      + "LIMIT 1")
  Optional<String> getInterlocutorProfile(String email, String chatId);
}
