package dev.dolu.chat_service.Service;


import dev.dolu.chat_service.Service.UserServiceClient;
import dev.dolu.chat_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestUserServiceClient implements UserServiceClient {


    private final RestTemplate restTemplate;

    public RestUserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean validateUser(String userId) {
        try {
            String url = "http://localhost:8080/api/users/" + userId; // Adjust as needed
            restTemplate.getForObject(url, User.class);
            return true;
        } catch (Exception e) {
            return false; // Fail gracefully if user does not exist
        }
    }

    @Override
    public User getUserDetails(String userId) {
        String url = "http://localhost:8080/api/users/" + userId; // Adjust as needed
        return restTemplate.getForObject(url, User.class);
    }
}
