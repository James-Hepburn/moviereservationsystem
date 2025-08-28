package com.example.moviereservationsystem.service;

import com.example.moviereservationsystem.model.User;
import com.example.moviereservationsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getUserById (Long id) {
        return this.userRepository.findById (id)
                .orElseThrow (() -> new RuntimeException ("User not found"));
    }

    public User getUserByUsername (String username) {
        return this.userRepository.findByUsername (username)
                .orElseThrow (() -> new RuntimeException ("User not found"));
    }

    public List <User> getAllUsers () {
        return this.userRepository.findAll ();
    }

    public User updateUser (Long id, User updatedUser) {
        User user = getUserById (id);

        user.setUsername (updatedUser.getUsername ());
        user.setEmail (updatedUser.getEmail ());
        user.setPassword (updatedUser.getPassword ());
        user.setRoles (updatedUser.getRoles ());

        return this.userRepository.save (user);
    }

    public void deleteUser (Long id) {
        User user = getUserById (id);
        this.userRepository.delete (user);
    }
}