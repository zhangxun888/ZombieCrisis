package com.zombiecrisis.service;

import com.zombiecrisis.entity.User;
import com.zombiecrisis.dto.UserDto;
import java.util.Optional;

public interface UserService {
    User registerUser(UserDto userDto);
    boolean activateUser(String token);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
