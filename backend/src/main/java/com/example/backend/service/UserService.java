package com.example.backend.service;

import com.example.backend.pojo.Role;
import com.example.backend.pojo.User;
import com.example.backend.repo.RoleRepository;
import com.example.backend.repo.UserRepository;
import com.example.backend.security.UserPrinciple;
import com.example.backend.security.dto.UserRegisterDTO;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    @Autowired
    @Qualifier("myPasswordEncoder")
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    public User save(UserRegisterDTO userRegisterDTO) {
        userRegisterDTO.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        User user = modelMapper.map(userRegisterDTO, User.class);

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role studentRole = roleRepository.findByName("student")
                    .orElseThrow(() -> new RuntimeException("Default role 'student' not found in the database"));
            user.setRoles(Set.of(studentRole));
        }
        return userRepository.save(user);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsernameWithRoles(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        else {
            User user = userOptional.get();
            List<Role> roles = roleRepository.findRolesByUserId(user.getId());
            Set<Role> setRoles = roles.stream()
                    .peek(role -> role.setName("ROLE_" + role.getName().toUpperCase()))
                    .collect(Collectors.toSet());
            user.setRoles(setRoles);
        }
        return UserPrinciple.build(userOptional.get());
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsernameWithRoles(username);
    }
}
