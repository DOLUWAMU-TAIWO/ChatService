package dev.dolu.chat_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users") // Maps this class to the "users" collection in MongoDB
public class User {

    @Id
    private String id; // MongoDB automatically generates a unique ID

    private String username; // Stores the username of the user
    private String email;    // Stores the user's email

    public User(String number, String mockUser1, String mail) {
        this.email= mail;
        this.id =number;
        this.username =mockUser1;
    }

    public User() {

    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
