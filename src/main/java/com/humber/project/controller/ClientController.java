package com.humber.project.controller;

import com.humber.project.model.Users;
import com.humber.project.repository.UsersRepository;
import com.humber.project.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ClientController {

    private final UsersService usersService;

    public ClientController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/")
    public String index(){
        return("index");
    }

    @Autowired
    private UsersRepository usersRepository;
    @PostMapping("/")
    public String authenticateUser(@RequestParam String username, @RequestParam String password, @RequestParam(required = false) String isAdmin, HttpSession session, RedirectAttributes redirectAttrs) {
        UsersService usersService = new UsersService(usersRepository);
        Users users = usersService.getUserByUsername(username, password);

        if (users != null && users.getPassword().equals(password)) {
            session.setAttribute("loggedUser", users);

            if ("on".equals(isAdmin)) {
                session.setAttribute("isAdmin", true);
                session.setAttribute("isUser", false);
            } else {
                session.setAttribute("isAdmin", false);
                session.setAttribute("isUser", true);
            }

            return "redirect:/home";
        } else {
            redirectAttrs.addFlashAttribute("error", "Invalid login credentials. Please try again");
            return "index";
        }
    }
    @GetMapping("/register")
    public String registerPage(){
        return "register";
    }
}
