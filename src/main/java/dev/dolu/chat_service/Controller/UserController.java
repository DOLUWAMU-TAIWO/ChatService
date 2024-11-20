package dev.dolu.chat_service.Controller;


import dev.dolu.chat_service.Repo.UserRepository;
import dev.dolu.chat_service.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        // Calls the repository to save the user
        User savedUser = userRepository.save(user);
        // Returns the saved User object with HTTP 201 status
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping // Maps to "/api/users"
    public ResponseEntity<List<User>> getAllUsers() {
        User sample = new User();
        sample.setEmail("Babafoto@jmail.org");
        sample.setUsername("Babafoto");
        userRepository.save(sample);
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable String id) {
        Optional<User> foundUser = userRepository.findById(id);

        // Check if the user exists; return 404 if not found
        if (foundUser.isPresent()) {
            return ResponseEntity.ok(foundUser.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }




}
