package com.example.moviereservationsystem.controller;

import com.example.moviereservationsystem.model.User;
import com.example.moviereservationsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUserById (@PathVariable Long id) {
        return this.userService.getUserById (id);
    }

    @GetMapping("/username/{username}")
    public User getUserByUsername (@PathVariable String username) {
        return this.userService.getUserByUsername (username);
    }

    @GetMapping
    public List <User> getAllUsers () {
        return this.userService.getAllUsers ();
    }

    @PutMapping("/{id}")
    public User updateUser (@PathVariable Long id, @RequestBody User updatedUser) {
        return this.userService.updateUser (id, updatedUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser (@PathVariable Long id) {
        this.userService.deleteUser (id);
    }
}