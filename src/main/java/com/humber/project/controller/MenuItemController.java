package com.humber.project.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.humber.project.service.MenuItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.humber.project.model.MenuItem;


@Controller
public class MenuItemController {

    private final MenuItemService menuItemService;

    @Autowired
    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("isAdmin") != null || session.getAttribute("isUser") != null;
    }

    private boolean isAdmin(HttpSession session) {
        return Boolean.TRUE.equals(session.getAttribute("isAdmin"));
    }

    @GetMapping("/home")
    public String viewHomePage(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/";
        }
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());
        return "home";
    }

    @GetMapping("/add")
    public String addMenuItemForm(Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }
        return "add";
    }

    @PostMapping("/add")
    public String submitMenuItemForm(@ModelAttribute MenuItem menuItem, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }
        menuItemService.addMenuItem(menuItem);
        return "redirect:/home";
    }

    @GetMapping("/edit/{id}")
    public String editMenuItemForm(@PathVariable Integer id, Model model, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }
        MenuItem menuItem = menuItemService.getMenuItemById(id);
        model.addAttribute("menuItem", menuItem);
        return "edit";
    }

    @PostMapping("/edit")
    public String submitEditMenuItemForm(@ModelAttribute MenuItem menuItem, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }
        menuItemService.editMenuItem(menuItem);
        return "redirect:/home";
    }

    @PostMapping("/delete-item/{id}")
    public String deleteItem(@PathVariable Integer id, HttpSession session) {
        if (!isAdmin(session)) {
            return "redirect:/";
        }
        menuItemService.deleteMenuItem(id);
        return "redirect:/home";
    }
}



