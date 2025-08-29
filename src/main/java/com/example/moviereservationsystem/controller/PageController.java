package com.example.moviereservationsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/")
    public String indexPage () {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage () {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage () {
        return "register";
    }

    @GetMapping("/test")
    public String testPage() {
        return "login";
    }
}
