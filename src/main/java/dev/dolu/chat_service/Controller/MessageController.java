package dev.dolu.chat_service.Controller;

import dev.dolu.chat_service.Repo.MessageRepository;
import dev.dolu.chat_service.Service.UserServiceClient;
import dev.dolu.chat_service.model.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Message messageRequest) {
        // Validate sender
        if (!userServiceClient.validateUser(messageRequest.getSenderId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Fetch sender details
        var user = userServiceClient.getUserDetails(messageRequest.getSenderId());
        if (user != null) {
            messageRequest.setSenderUsername(user.getUsername()); // Set sender username
        }

        // Save the message
        Message savedMessage = messageRepository.save(messageRequest);

        // Return the saved message
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMessage);
    }


    @GetMapping("/{chatId}")
    public ResponseEntity<?> getMessagesByChatId(@PathVariable String chatId) {
        // Retrieve messages by chatId and include sender details
        List<Message> messages = messageRepository.findByChatId(chatId);
        messages.forEach(message -> {
            var user = userServiceClient.getUserDetails(message.getSenderId());
            message.setSenderUsername(user.getUsername());
        });

        return ResponseEntity.ok(messages);
    }
}
