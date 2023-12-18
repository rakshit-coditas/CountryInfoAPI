package com.example.countryinfoapi.service.impl;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Hardcoded username and password for simplicity
        if ("abcd".equals(username)) {
            String encodedPassword = new BCryptPasswordEncoder().encode("abcd");
            return User.withUsername("abcd")
                .password(encodedPassword)
                .roles("USER")
                .build();
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}

