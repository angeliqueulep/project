package com.humber.project.controller;

import com.humber.project.model.Users;
import com.humber.project.repository.UsersRepository;
import com.humber.project.service.UsersService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String authenticateUser(@ModelAttribute("loggedUser") @RequestParam String username, @RequestParam String password, HttpSession session) {
        UsersService usersService = new UsersService(usersRepository);
        Users users = usersService.getUserByUsername(username, password);

        if (users != null && users.getPassword().equals(password)) {
            String role = users.getRole(); // Fetch role from the retrieved user

            if ("admin".equals(role)) {
                session.setAttribute("isAdmin", true);
            } else {
                session.setAttribute("isUser", true);
            }

            return "redirect:/home";
        } else {
            //redirectAttrs.addFlashAttribute("error", "Invalid login credentials. Please try again");
            return "redirect:/?failed";
        }
    }
    @GetMapping("/register")
    public String registerPage(Model model)
    {
        Users user = new Users();
        model.addAttribute("users", user);
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("users") Users user, BindingResult result, Model model) {

        Users existingUser = usersService.isUserExists(user.getUsername());

        if (existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()) {
            System.out.println("Error");
            result.rejectValue("username", null, "Username already exists");
        }

        if (result.hasErrors()) {
            model.addAttribute("users", user);
            return "/register";
        }

        System.out.println(usersService.getAllUsers());
        usersService.saveUser(user);
        return "redirect:/register?success";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
