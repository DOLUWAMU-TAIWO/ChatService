package dev.dolu.chat_service.Controller;

import dev.dolu.chat_service.Repo.ChatRepository;
import dev.dolu.chat_service.Service.UserServiceClient;
import dev.dolu.chat_service.model.Chat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    /**
     * Endpoint to start a new private chat.
     *
     * @param chatRequest Chat payload containing participants.
     * @return ResponseEntity with the created chat or error.
     */
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @PostMapping
    public ResponseEntity<?> startChat(@RequestBody Chat chatRequest) {
        logger.info("Received request to start chat with participants: {}", chatRequest.getParticipants());

        List<String> participants = chatRequest.getParticipants();
        if (participants == null || participants.size() != 2) {
            logger.warn("Invalid participants: {}", participants);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Private chats must have exactly two participants.");
        }

        for (String participantId : participants) {
            logger.debug("Validating participant ID: {}", participantId);
            try {
                if (!userServiceClient.validateUser(participantId)) {
                    logger.warn("Participant validation failed for ID: {}", participantId);
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Invalid participant ID: " + participantId);
                }
            } catch (Exception e) {
                logger.error("Error validating participant ID {}: {}", participantId, e.getMessage());
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("User Service is unavailable. Please try again later.");
            }
        }

        Chat savedChat = chatRepository.save(chatRequest);
        logger.info("Chat successfully created: {}", savedChat);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedChat);
    }

    /**
     * Endpoint to retrieve a chat by ID.
     *
     * @param chatId The ID of the chat.
     * @return ResponseEntity with the chat details.
     */
    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> getChatById(@PathVariable String chatId) {
        return chatRepository.findById(chatId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}

