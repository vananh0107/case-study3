package com.example.backend.service;

import com.example.backend.dto.UserRegisterDTO;
import com.example.backend.pojo.User;
import com.example.backend.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void save(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        user.setFullName(userRegisterDTO.getFullName());
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(userRegisterDTO.getPassword()));
        user.setRole("ROLE_USER");
        user.setVerifiedEmail(false);
        log.info("save user {}", user);
        userRepository.save(user);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
