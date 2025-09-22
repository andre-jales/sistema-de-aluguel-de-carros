package com.aluguelcarros.aluguel_carros.controller;

import com.aluguelcarros.aluguel_carros.model.*;
import com.aluguelcarros.aluguel_carros.service.OrderService;
import com.aluguelcarros.aluguel_carros.service.CustomerService;
import com.aluguelcarros.aluguel_carros.service.AgentService;
import com.aluguelcarros.aluguel_carros.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AgentService agentService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @GetMapping
    public String listOrders(Model model) {
        List<Order> orders = orderService.findAll();
        model.addAttribute("orders", orders);
        return "orders";
    }

    @GetMapping("/new")
    public String newOrder(Model model) {
        model.addAttribute("order", new Order());
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("vehicles", vehicleRepository.findByAvailableTrue());
        return "order-form";
    }

    @PostMapping("/save")
    public String saveOrder(@ModelAttribute Order order,
            @RequestParam Long customerId,
            @RequestParam Long vehicleId,
            RedirectAttributes redirectAttributes) {
        try {
            orderService.createOrder(customerId, vehicleId, order);
            redirectAttributes.addFlashAttribute("success", "Order created successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String editOrder(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        model.addAttribute("order", order);
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("vehicles", vehicleRepository.findAll());
        return "order-form";
    }

    @PostMapping("/update/{id}")
    public String updateOrder(@PathVariable Long id, @ModelAttribute Order order,
            RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrder(id, order);
            redirectAttributes.addFlashAttribute("success", "Order updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("success", "Order deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders";
    }

    @GetMapping("/assign-agent/{id}")
    public String assignAgent(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        model.addAttribute("order", order);
        model.addAttribute("agents", agentService.findAll());
        return "assign-agent";
    }

    @PostMapping("/assign-agent/{id}")
    public String assignAgent(@PathVariable Long id, @RequestParam Long agentId,
            RedirectAttributes redirectAttributes) {
        try {
            orderService.assignAgent(id, agentId);
            redirectAttributes.addFlashAttribute("success", "Agent assigned successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders";
    }

    @GetMapping("/approve/{id}")
    public String approveOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.approveOrder(id);
            redirectAttributes.addFlashAttribute("success", "Order approved successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders";
    }

    @GetMapping("/reject/{id}")
    public String rejectOrder(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        model.addAttribute("order", order);
        return "reject-order";
    }

    @PostMapping("/reject/{id}")
    public String rejectOrder(@PathVariable Long id, @RequestParam String reason,
            RedirectAttributes redirectAttributes) {
        try {
            orderService.rejectOrder(id, reason);
            redirectAttributes.addFlashAttribute("success", "Order rejected successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders";
    }

    @GetMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id);
            redirectAttributes.addFlashAttribute("success", "Order canceled successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/orders";
    }

    @GetMapping("/details/{id}")
    public String orderDetails(@PathVariable Long id, Model model) {
        Order order = orderService.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        model.addAttribute("order", order);
        return "order-details";
    }
}