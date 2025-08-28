package com.example.moviereservationsystem.security;

import com.example.moviereservationsystem.model.Role;
import com.example.moviereservationsystem.model.User;
import com.example.moviereservationsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername (String usernameOrEmail) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername (usernameOrEmail)
                .orElse (this.userRepository.findByEmail (usernameOrEmail)
                .orElseThrow (() -> new UsernameNotFoundException ("User not found")));
        return org.springframework.security.core.userdetails.User
                .withUsername (user.getUsername ())
                .password (user.getPassword ())
                .authorities (user.getRoles ().stream ()
                                .map (Role::getName)
                                .map (SimpleGrantedAuthority::new)
                                .collect (Collectors.toList ()))
                .build ();
    }
}