package com.humber.project.controller;

import ch.qos.logback.core.model.Model;
import com.humber.project.model.User;
import com.humber.project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClientController {

    private final UserService userService;

    public ClientController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index(){
        return("index");
    }

    @PostMapping("/")
    public String authenticateUser(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) String isAdmin, HttpSession session, RedirectAttributes redirectAttrs) {
        User user = userService.getUserByUsername(username,password);
        System.out.println(username);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("loggedUser", user);

            if ("on".equals(isAdmin)) {
                session.setAttribute("isAdmin", true);
                session.setAttribute("isUser", false);
            } else {
                session.setAttribute("isAdmin", false);
                session.setAttribute("isUser", true);
            }

            return "redirect:/success";
        } else {
            redirectAttrs.addFlashAttribute("error","Invalid login credentials. Please try again");
            return "index";
        }
    }
    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }
}
