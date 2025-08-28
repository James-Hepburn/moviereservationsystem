package com.example.moviereservationsystem.controller;

import com.example.moviereservationsystem.model.User;
import com.example.moviereservationsystem.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public String login (@RequestParam String usernameOrEmail, @RequestParam String password) {
        return this.authService.login (usernameOrEmail, password);
    }

    @PostMapping("/register")
    public User register (@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        return this.authService.register (username, email, password);
    }

    @PostMapping("/promote/{userId}")
    public void promoteToAdmin (@PathVariable Long userId) {
        this.authService.promoteToAdmin (userId);
    }
}