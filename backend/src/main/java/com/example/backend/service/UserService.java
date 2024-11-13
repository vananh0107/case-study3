package com.example.backend.service;

import com.example.backend.dto.UserRegisterDTO;
import com.example.backend.pojo.User;
import com.example.backend.pojo.Verify;
import com.example.backend.repo.UserRepository;
import com.example.backend.repo.VerifyRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;


@Slf4j
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerifyRepository verifyRepository;

    public void save(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        user.setFullName(userRegisterDTO.getFullName());
        user.setUsername(userRegisterDTO.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(userRegisterDTO.getPassword()));
        user.setRole("ROLE_STUDENT");
        user.setVerifiedEmail(false);
        userRepository.save(user);
        generateVerificationCode(user);
    }

    public void generateVerificationCode(User user) {
        Verify existingVerify = verifyRepository.findByUser(user);
        String code = String.format("%05d", new Random().nextInt(100000));
        LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(10);
        if (existingVerify != null) {
            existingVerify.setCode(code);
            existingVerify.setExpiryDate(expiryDate);
            verifyRepository.save(existingVerify);
        } else {
            Verify newVerify = new Verify();
            newVerify.setCode(code);
            newVerify.setExpiryDate(expiryDate);
            newVerify.setUser(user);
            verifyRepository.save(newVerify);
        }
        emailService.sendEmail(user.getUsername(), "Code for verify", code);
    }

    @Transactional
    public boolean verifyCode(User user, String code) {
        Verify verify = verifyRepository.findByUser(user);
        if (verify != null && verify.getCode().equals(code) && verify.getExpiryDate().isAfter(LocalDateTime.now())) {
            user.setVerifiedEmail(true);
            userRepository.save(user);
            verifyRepository.delete(verify);
            return true;
        }
        return false;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}
