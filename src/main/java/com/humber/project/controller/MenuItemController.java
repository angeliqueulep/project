package com.humber.project.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.humber.project.service.MenuItemService;
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

    @GetMapping("/add")
    public String addMenuItemForm(Model model) {
        return "add";
    }

    @PostMapping("/add")
    public String submitMenuItemForm(@ModelAttribute MenuItem menuItem, Model model) {
        menuItemService.addMenuItem(menuItem);
        return "redirect:/home";
    }

    @GetMapping("/edit/{id}")
    public String editMenuItemForm(@PathVariable Integer id, Model model) {
        MenuItem menuItem = menuItemService.getMenuItemById(id);
        model.addAttribute("menuItem", menuItem);
        return "edit";
    }

    @PostMapping("/edit")
    public String submitEditMenuItemForm(@ModelAttribute MenuItem menuItem, Model model) {
        menuItemService.editMenuItem(menuItem);
        return "redirect:/home";
    }
}



