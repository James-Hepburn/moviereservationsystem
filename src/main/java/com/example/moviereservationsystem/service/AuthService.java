package com.example.moviereservationsystem.service;

import com.example.moviereservationsystem.model.Role;
import com.example.moviereservationsystem.model.User;
import com.example.moviereservationsystem.repository.UserRepository;
import com.example.moviereservationsystem.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String login (String usernameOrEmail, String password) {
        try {
            this.authenticationManager.authenticate (new UsernamePasswordAuthenticationToken (usernameOrEmail, password));
            User user = this.userRepository.findByUsername (usernameOrEmail)
                    .orElse (this.userRepository.findByEmail (usernameOrEmail)
                            .orElseThrow (() -> new RuntimeException ("User not found")));
            return this.jwtUtil.generateToken (org.springframework.security.core.userdetails.User
                            .withUsername (user.getUsername ())
                            .password (user.getPassword ())
                            .authorities (user.getRoles ().stream ()
                                    .map (Role::getName)
                                    .map (r -> new org.springframework.security.core.authority.SimpleGrantedAuthority (r))
                                    .toList ())
                            .build ());
        } catch (AuthenticationException e) {
            throw new RuntimeException ("Invalid credentials");
        }
    }

    public User register (String username, String email, String password) {
        if (this.userRepository.existsByUsername (username)) {
            throw new RuntimeException ("Username already exists");
        }else if (this.userRepository.existsByEmail (email)) {
            throw new RuntimeException ("Email already exists");
        }

        Set <Role> roles = new HashSet<>();
        roles.add (new Role ("ROLE_USER"));

        User user = new User (username, email, this.passwordEncoder.encode (password), roles);
        return this.userRepository.save (user);
    }

    public void promoteToAdmin (Long userId) {
        if (!this.userRepository.existsById (userId)) {
            throw new RuntimeException ("User not found");
        }

        User user = this.userRepository.findById (userId).get ();
        user.getRoles ().add (new Role ("ROLE_ADMIN"));
        this.userRepository.save (user);
    }
}