package dev.dolu.chat_service.Repo;

import dev.dolu.chat_service.model.EncryptionKey;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface KeyRepository extends MongoRepository<EncryptionKey, String> {
    EncryptionKey findByKeyVersion(int keyVersion);
}
