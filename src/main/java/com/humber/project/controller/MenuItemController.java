package com.humber.project.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.humber.project.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class MenuItemController {

    private final MenuItemService menuItemService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("/home")
    public String viewHomePage(Model model) {
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());
        return "home";
    }

    @PostMapping("/delete-item/{id}")
    public String deleteItem(@PathVariable Integer id) {
        menuItemService.deleteMenuItem(id);
        return "redirect:/home";
    }

}



