package com.humber.project.controller;

import com.humber.project.service.MenuItemService;
import com.humber.project.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.humber.project.service.MenuItemService;
import com.humber.project.service.OrderService;
import com.humber.project.model.Order;
import com.humber.project.model.OrderItem;
import com.humber.project.model.MenuItem;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;

import java.util.Optional;


@Controller
@SessionAttributes("order")
public class OrderController {
    private final MenuItemService menuItemService;
    private final OrderService orderService;

    public OrderController(MenuItemService menuItemService, OrderService orderService) {
        this.menuItemService = menuItemService;
        this.orderService = orderService;
    }

    private boolean isAuthenticated(HttpSession session) {
        return session.getAttribute("isAdmin") != null || session.getAttribute("isUser") != null;
    }

    @GetMapping("/order_page")
    public String orderPage(Model model) {
        if (!model.containsAttribute("order")) {
            model.addAttribute("order", new Order());
        }
        model.addAttribute("menuItems", menuItemService.getAllMenuItems());
        return "order_page";
    }

    @PostMapping("/add_to_order/{id}")
    public String addToOrder(@PathVariable Integer id, Model model, HttpServletRequest request) {
        Order order = (Order) model.getAttribute("order");

        MenuItem menuItem = menuItemService.getMenuItemById(id);
        if (menuItem != null) {
            Optional<OrderItem> existingItem = order.getOrderItems().stream()
                    .filter(item -> item.getMenuItem().getId().equals(menuItem.getId()))
                    .findFirst();

            if (existingItem.isPresent()) {
                OrderItem orderItem = existingItem.get();
                orderItem.setQuantity(orderItem.getQuantity() + 1);
            } else {
                OrderItem newOrderItem = new OrderItem();
                newOrderItem.setMenuItem(menuItem);
                newOrderItem.setQuantity(1);
                newOrderItem.setItemPrice(menuItem.getPrice());
                newOrderItem.setOrder(order);
                order.getOrderItems().add(newOrderItem);
            }

            order.setTotalPrice(order.calculateTotalPrice(order.getOrderItems()));

            orderService.saveOrder(order);
        }

        String referer = request.getHeader("Referer");
        return referer != null ? "redirect:" + referer : "redirect:/order_page";

    }

    @PostMapping("/remove_from_order/{id}")
    public String removeFromOrder(@PathVariable Integer id, Model model, HttpServletRequest request) {
        Order order = (Order) model.getAttribute("order");

        MenuItem menuItem = menuItemService.getMenuItemById(id);
        if (menuItem != null) {
            Optional<OrderItem> existingItem = order.getOrderItems().stream()
                    .filter(item -> item.getMenuItem().getId().equals(menuItem.getId()))
                    .findFirst();

            if (existingItem.isPresent()) {
                OrderItem orderItem = existingItem.get();
                orderItem.setQuantity(orderItem.getQuantity() - 1);
                if (orderItem.getQuantity() == 0) {
                    order.getOrderItems().remove(orderItem);
                }
            }

            order.setTotalPrice(order.calculateTotalPrice(order.getOrderItems()));

            orderService.saveOrder(order);
        }

        String referer = request.getHeader("Referer");
        return referer != null ? "redirect:" + referer : "redirect:/order_page";

    }

    @GetMapping("/view_cart")
    public String viewCart(Model model) {
        Order order = (Order) model.getAttribute("order");
        if (order == null) {
            return "redirect:/order_page";
        }
        model.addAttribute("orderItems", order.getOrderItems());
        model.addAttribute("totalPrice", order.getTotalPrice());
        return "view_cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        Order order = (Order) model.getAttribute("order");
        if (order == null) {
            return "redirect:/order_page";
        }
        order.setStatus("Pending");
        orderService.saveOrder(order);
        return "checkout";
    }

    @GetMapping("/mark_as_pending/{id}")
    public String markAsPending(@PathVariable Integer id, Model model, HttpServletRequest request) {
        Order order = orderService.getOrderById(id);
        String referer = request.getHeader("Referer");
        if (order == null) {
            return referer != null ? "redirect:" + referer : "redirect:/view_orders";
        }
        order.setStatus("Pending");
        orderService.saveOrder(order);
        return referer != null ? "redirect:" + referer : "redirect:/view_orders";
    }

    @GetMapping("/mark_as_processing/{id}")
    public String markAsProcessing(@PathVariable Integer id, Model model, HttpServletRequest request) {
        Order order = orderService.getOrderById(id);
        String referer = request.getHeader("Referer");
        if (order == null) {
            return referer != null ? "redirect:" + referer : "redirect:/view_orders";
        }
        order.setStatus("Processing");
        orderService.saveOrder(order);
        return referer != null ? "redirect:" + referer : "redirect:/view_orders";
    }

    @GetMapping("/mark_as_delivered/{id}")
    public String markAsCompleted(@PathVariable Integer id, Model model, HttpServletRequest request) {
        Order order = orderService.getOrderById(id);
        String referer = request.getHeader("Referer");
        if (order == null) {
            return referer != null ? "redirect:" + referer : "redirect:/view_orders";
        }
        order.setStatus("Delivered");
        orderService.saveOrder(order);
        return referer != null ? "redirect:" + referer : "redirect:/view_orders";
    }

    @GetMapping("/view_orders")
    public String viewOrders(Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/homepage";
        }
        model.addAttribute("orders", orderService.getAllOrders());
        return "view_orders";
    }

    @GetMapping("/view_order/{id}")
    public String viewOrder(@PathVariable Integer id, Model model, HttpSession session) {
        if (!isAuthenticated(session)) {
            return "redirect:/homepage";
        }
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return "redirect:/view_orders";
        }
        model.addAttribute("order", order);
        model.addAttribute("orderItems", order.getOrderItems());
        model.addAttribute("totalPrice", order.getTotalPrice());
        return "view_order";
    }
}
