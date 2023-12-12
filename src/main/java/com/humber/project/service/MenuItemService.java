package com.humber.project.service;

import com.humber.project.model.MenuItem;
import com.humber.project.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public void deleteMenuItem(Integer id) {
        menuItemRepository.deleteById(id);
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItemRepository.save(menuItem);
    }

    public MenuItem getMenuItemById(Integer id) {
        return menuItemRepository.findById(id).get();
    }

    public void editMenuItem(MenuItem menuItem) {
        if (menuItem.getId() != null) {
            menuItemRepository.save(menuItem);
        }
    }

}
