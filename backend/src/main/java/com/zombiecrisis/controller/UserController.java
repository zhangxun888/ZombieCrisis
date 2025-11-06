package com.zombiecrisis.controller;

import com.zombiecrisis.entity.User;
import com.zombiecrisis.dto.UserDto;
import com.zombiecrisis.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserDto userDto) {
        if (userService.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }
        if (userService.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }
        User user = userService.registerUser(userDto);
        // In a real application, you would send an email with the activation token
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully. Activation token: " + user.getActivationToken());
    }

    @GetMapping("/activate/{token}")
    public ResponseEntity<String> activateUser(@PathVariable String token) {
        boolean activated = userService.activateUser(token);
        if (activated) {
            return ResponseEntity.ok("User activated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired activation token");
        }
    }
}
