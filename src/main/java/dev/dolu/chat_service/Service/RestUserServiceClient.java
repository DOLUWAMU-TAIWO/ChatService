package dev.dolu.chat_service.Service;

import dev.dolu.chat_service.Service.UserServiceClient;
import dev.dolu.chat_service.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class RestUserServiceClient implements UserServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(RestUserServiceClient.class);

    private final RestTemplate restTemplate;

    public RestUserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validateUser(String userId) {
        try {
            String url = "http://localhost:8080/api/users/" + Long.parseLong(userId);

            logger.debug("Calling User Service to validate user ID: {}", userId);

            ResponseEntity<Object> response = restTemplate.getForEntity(url, Object.class);
            logger.debug("User Service response for user ID {}: {} - Body: {}", userId, response.getStatusCode(), response.getBody());

            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.warn("User Service returned 404 for user ID: {}", userId);
            } else {
                logger.error("HTTP error validating user ID {}: {}", userId, e.getMessage());
            }
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error validating user ID {}: {}", userId, e.getMessage());
            return false;
        }
    }


    @Override
    public User getUserDetails(String userId) {
        try {
            String url = "http://localhost:8080/api/users/" + userId;
            logger.debug("Fetching user details from User Service for user ID: {}", userId);

            User user = restTemplate.getForObject(url, User.class);
            logger.debug("User details retrieved for user ID {}: {}", userId, user);
            return user;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.warn("User Service returned 404 for user ID: {}", userId);
            } else {
                logger.error("HTTP error fetching user details for user ID {}: {}", userId, e.getMessage());
            }
            return null;
        } catch (Exception e) {
            logger.error("Unexpected error fetching user details for user ID {}: {}", userId, e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            String url = "http://localhost:8080/api/users/user-details";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            logger.debug("Validating token by calling User Service at /user-details");
            ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Object.class);

            logger.debug("Token validation successful: {}", response.getStatusCode());
            return true;
        } catch (HttpClientErrorException e) {
            logger.warn("Token validation failed: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            logger.error("Unexpected error during token validation: {}", e.getMessage());
            return false;
        }
    }
}
