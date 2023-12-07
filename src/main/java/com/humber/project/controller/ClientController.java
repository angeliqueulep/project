package com.humber.project.controller;

import com.humber.project.model.User;
import com.humber.project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClientController {

    private final UserService userService;

    public ClientController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }
    @PostMapping("/")
    public String authenticateUser(@RequestParam String email, @RequestParam String password) {
        User user = userService.authenticateUser(email, password);

        if (user != null) {
            // Redirect to home page or perform further actions upon successful login
            return "redirect:/home";
        } else {
            return "redirect:/?error=Invalid credentials. Please try again.";
        }
    }
    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }
}
