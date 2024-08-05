package com.example.item_manager.controller;

import com.example.item_manager.model.User;
import com.example.item_manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userService.existsByUsername(user.getUsername()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");

        User savedUser = userService.save(user);
        String responseMessage = String.format("User registered successfully: {\"id\": %d, \"username\": \"%s\"}",
                savedUser.getId(),
                savedUser.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        userService.updateUser(id, userDetails);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<User> changePassword(@PathVariable Long id, @RequestBody String newPassword) {
        userService.changePassword(id, newPassword);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
