package dev.dolu.chat_service.Repo;

import dev.dolu.chat_service.model.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, String> {
    // Custom query methods can be added here, e.g.:
    // List<Chat> findByParticipantsContaining(String userId);
}
