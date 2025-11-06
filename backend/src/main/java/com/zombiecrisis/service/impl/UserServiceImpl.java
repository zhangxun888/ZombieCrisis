package com.zombiecrisis.service.impl;

import com.zombiecrisis.entity.User;
import com.zombiecrisis.dto.UserDto;
import com.zombiecrisis.repository.UserRepository;
import com.zombiecrisis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setActivationToken(UUID.randomUUID().toString());
        user.setActivationExpiry(LocalDateTime.now().plusHours(24));
        return userRepository.save(user);
    }

    @Override
    public boolean activateUser(String token) {
        Optional<User> optionalUser = userRepository.findByActivationToken(token);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getActivationExpiry().isAfter(LocalDateTime.now())) {
                user.setEnabled(true);
                user.setActivationToken(null);
                user.setActivationExpiry(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
