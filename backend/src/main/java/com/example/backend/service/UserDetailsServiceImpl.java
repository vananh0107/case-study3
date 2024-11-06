package com.example.backend.service;

import com.example.backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        com.example.backend.pojo.User userSystem = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//        if (!userSystem.getActive()) {
//            throw new InternalAuthenticationServiceException("User account is not active");
//        }
        return User.withUsername(userSystem.getUsername())
                .password(userSystem.getPassword())
                .authorities(AuthorityUtils.createAuthorityList(userSystem.getRole()))
                .build();
    }
}
