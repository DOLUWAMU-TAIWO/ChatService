package dev.dolu.chat_service.Repo;

import dev.dolu.chat_service.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message,String> {

    List<Message> findByChatId(String chatId);

}
