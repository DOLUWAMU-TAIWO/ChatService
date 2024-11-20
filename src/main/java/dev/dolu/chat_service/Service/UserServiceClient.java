package dev.dolu.chat_service.Service;

import dev.dolu.chat_service.model.User;

public interface UserServiceClient {
    boolean validateUser(String userId); // Check if user exists
    User getUserDetails(String userId); // Fetch user details
}
