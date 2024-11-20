package dev.dolu.chat_service.Controller;


import dev.dolu.chat_service.Repo.ChatRepository;
import dev.dolu.chat_service.model.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @PostMapping
    ResponseEntity<Chat> startChat(@RequestBody Chat chatRequest) {

        if (chatRequest.getParticipants() == null || chatRequest.getParticipants().size() < 2) {
            return ResponseEntity.badRequest().build();
        }
        Chat savedChat = chatRepository.save(chatRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedChat);
    }
}
