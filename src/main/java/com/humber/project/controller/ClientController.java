package com.humber.project.controller;

import com.humber.project.model.User;
import com.humber.project.service.UserService;
import jakarta.servlet.http.HttpSession;
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

    @PostMapping("/")
    public String authenticateUser(@RequestParam String email, @RequestParam String password, HttpSession session) {
        User user = userService.authenticateUser(email, password);

        if (user != null) {
            session.setAttribute("loggedUser", user);

            if (user.isAdmin()) {
                session.setAttribute("isAdmin", true);
                session.setAttribute("isUser", false);
            } else {
                session.setAttribute("isAdmin", false);
                session.setAttribute("isUser", true);
            }

            return "redirect:/success";
        } else {
            return "redirect:/?error=Invalid credentials. Please try again.";
        }
    }
    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }
}
