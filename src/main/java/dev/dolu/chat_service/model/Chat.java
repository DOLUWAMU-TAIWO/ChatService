package dev.dolu.chat_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Document(collection = "chats")
public class Chat {

    @Id
    private final String id; // Unique chat ID (auto-generated)

    private String name; // Name of the chat (optional, can be null for private chats)

    private List<String> participants; // List of two user IDs participating in the chat

    // Default constructor for MongoDB
    public Chat() {
        this.id = UUID.randomUUID().toString();
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}
