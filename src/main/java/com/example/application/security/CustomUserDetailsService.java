package com.example.application.security;

import com.example.application.mapper.UserMapper;
import com.example.application.persistence.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(userRepository.findByUsername(username).get().getUsername());
        return UserMapper.convertToUser(userRepository.findByUsername(username).orElseThrow(()-> new RuntimeException("User does not exist")));
    }
}
