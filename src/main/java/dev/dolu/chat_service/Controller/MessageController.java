package dev.dolu.chat_service.Controller;

import dev.dolu.chat_service.Repo.ChatRepository;
import dev.dolu.chat_service.Repo.MessageRepository;
import dev.dolu.chat_service.Service.KeyService;
import dev.dolu.chat_service.Service.UserServiceClient;
import dev.dolu.chat_service.model.Message;
import dev.dolu.chat_service.util.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private KeyService keyService;

    @Autowired
    private UserServiceClient userServiceClient; // Still used for fetching user details

    /**
     * Endpoint to send a message.
     *
     * @param messageRequest Message payload.
     * @return ResponseEntity with the saved message.
     */
    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Message messageRequest) {
        try {
            // Check if the chat exists
            var chat = chatRepository.findById(messageRequest.getChatId());
            if (chat.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chat not found.");
            }

            // Validate if the sender is a participant
            List<String> participants = chat.get().getParticipants();
            if (!participants.contains(messageRequest.getSenderId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sender is not a participant in this chat.");
            }

            // Fetch sender details to enrich the message
            var user = userServiceClient.getUserDetails(messageRequest.getSenderId());
            if (user != null) {
                messageRequest.setSenderUsername(user.getUsername());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid sender ID.");
            }

            // Fetch encryption key (assuming version 1 for simplicity)
            SecretKey aesKey = keyService.getKeyByVersion(1);
            if (aesKey == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Encryption key not found.");
            }

            // Encrypt the message content
            String encryptedContent = EncryptionUtil.encryptMessage(messageRequest.getContent(), aesKey);
            messageRequest.setEncryptedContent(encryptedContent); // Store encrypted content
            messageRequest.setKeyVersion(1); // Track the key version
            messageRequest.setContent(null); // Clear plain content for security

            // Save the message
            Message savedMessage = messageRepository.save(messageRequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
        } catch (Exception e) {
            logger.error("Error while sending the message.", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while sending the message.");
        }
    }

    /**
     * Endpoint to retrieve messages by chat ID.
     *
     * @param chatId The ID of the chat.
     * @return ResponseEntity with a list of decrypted messages.
     */
    @GetMapping("/{chatId}")
    public ResponseEntity<?> getMessagesByChatId(@PathVariable String chatId) {
        logger.info("Fetching messages for chatId: {}", chatId);

        try {
            // Fetch messages from the database
            List<Message> messages = messageRepository.findByChatId(chatId);
            logger.info("Number of messages retrieved: {}", messages.size());

            // Decrypt each message and enrich with sender details
            for (Message message : messages) {
                logger.debug("Processing message with ID: {}", message.getId());

                // Fetch encryption key
                SecretKey aesKey = keyService.getKeyByVersion(message.getKeyVersion());
                if (aesKey == null) {
                    throw new RuntimeException("Decryption key not found for version: " + message.getKeyVersion());
                }

                // Decrypt message content
                String decryptedContent = EncryptionUtil.decryptMessage(message.getEncryptedContent(), aesKey);
                message.setContent(decryptedContent);

                // Fetch sender details
                var user = userServiceClient.getUserDetails(message.getSenderId());
                if (user != null) {
                    message.setSenderUsername(user.getUsername());
                }
            }

            logger.info("Successfully processed all messages for chatId: {}", chatId);
            return ResponseEntity.ok(messages);
        } catch (Exception e) {
            logger.error("Error while retrieving messages for chatId: {}", chatId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving messages.");
        }
    }
}
