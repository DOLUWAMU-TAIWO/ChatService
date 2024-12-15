package dev.dolu.chat_service.Controller;

import dev.dolu.chat_service.Service.UserServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TokenValidationController {

    @Autowired
    private UserServiceClient userServiceClient;

    private static final Logger logger = LoggerFactory.getLogger(TokenValidationController.class);

    @GetMapping("/validate-token")
    public ResponseEntity<String> validateToken() {
        // If the request reaches this method, the token validation filter worked.
        return ResponseEntity.ok("Token is valid and the request is authenticated.");
    }


    @GetMapping("/validate-participant")
    public ResponseEntity<String> validateParticipant(@RequestParam String participantId) {
        logger.info("Validating participant ID: {}", participantId);
        try {
            // Call User Service to validate participant
            boolean isValid = userServiceClient.validateUser(participantId);
            if (isValid) {
                logger.info("Participant ID {} is valid.", participantId);
                return ResponseEntity.ok("Participant is valid.");
            } else {
                logger.warn("Participant ID {} is invalid.", participantId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Participant is invalid.");
            }
        } catch (Exception e) {
            logger.error("Error validating participant ID {}: {}", participantId, e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("Error validating participant. User Service may be unavailable.");
        }
    }



}
