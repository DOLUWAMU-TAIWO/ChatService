package dev.dolu.chat_service.Service;

import dev.dolu.chat_service.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocalUserServiceClient implements UserServiceClient {

    private final Map<String, User> mockUsers = new HashMap<>();

    public LocalUserServiceClient() {
        // Initialize with mock data
        mockUsers.put("1", new User("1", "MockUser1", "mock1@example.com"));
        mockUsers.put("2", new User("2", "MockUser2", "mock2@example.com"));
    }

    @Override
    public boolean validateUser(String userId) {
        return mockUsers.containsKey(userId);
    }

    @Override
    public User getUserDetails(String userId) {
        return mockUsers.getOrDefault(userId, null);
    }
}
