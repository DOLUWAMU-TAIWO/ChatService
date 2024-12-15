package dev.dolu.chat_service.Service;

import dev.dolu.chat_service.model.EncryptionKey;
import dev.dolu.chat_service.Repo.KeyRepository;
import dev.dolu.chat_service.util.EncryptionUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class KeyService {

    @Autowired
    private KeyRepository keyRepository;

    private final Map<Integer, SecretKey> keys = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            // Generate a key for version 1
            SecretKey key = EncryptionUtil.generateAESKey();
            keys.put(1, key); // Save key with version 1
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize encryption keys", e);
        }
    }

    public SecretKey getKeyByVersion(int version) {
        if (!keys.containsKey(version)) {
            throw new RuntimeException("Key not found for version: " + version);
        }
        return keys.get(version);
    }



    // Generate and store a new AES key
    public EncryptionKey generateAndStoreKey(int keyVersion) throws Exception {
        // Generate a new AES key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // AES-128
        SecretKey secretKey = keyGen.generateKey();

        // Convert the key to a Base64 string for storage
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        // Create an EncryptionKey object
        EncryptionKey encryptionKey = new EncryptionKey();
        encryptionKey.setKeyVersion(keyVersion);
        encryptionKey.setAesKey(encodedKey);

        // Save the key in MongoDB
        return keyRepository.save(encryptionKey);
    }

//    // Retrieve a key by version
//    public SecretKey getKeyByVersion(int keyVersion) {
//        // Fetch the key from MongoDB
//        EncryptionKey encryptionKey = keyRepository.findByKeyVersion(keyVersion);
//        if (encryptionKey == null) {
//            throw new RuntimeException("Key not found for version: " + keyVersion);
//        }
//
//        // Decode the Base64-encoded key into a SecretKey object
//        byte[] decodedKey = Base64.getDecoder().decode(encryptionKey.getAesKey());
//        return new javax.crypto.spec.SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
//    }
}
