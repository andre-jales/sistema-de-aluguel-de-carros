package com.aluguelcarros.aluguel_carros.controller;

import com.aluguelcarros.aluguel_carros.model.Employer;
import com.aluguelcarros.aluguel_carros.service.EmployerService;
import com.aluguelcarros.aluguel_carros.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/employers")
public class EmployerController {

    @Autowired
    private EmployerService employerService;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String listEmployers(Model model) {
        List<Employer> employers = employerService.findAll();
        model.addAttribute("employers", employers);
        return "employers";
    }

    @GetMapping("/new")
    public String newEmployer(Model model) {
        model.addAttribute("employer", new Employer());
        model.addAttribute("customers", customerService.findAll());
        return "employer-form";
    }

    @GetMapping("/new/{customerId}")
    public String newEmployerForCustomer(@PathVariable Long customerId, Model model) {
        Employer employer = new Employer();
        model.addAttribute("employer", employer);
        model.addAttribute("customerId", customerId);
        model.addAttribute("customers", customerService.findAll());
        return "employer-form";
    }

    @PostMapping("/save")
    public String saveEmployer(@ModelAttribute Employer employer,
            @RequestParam(required = false) Long customerId,
            RedirectAttributes redirectAttributes) {
        try {
            if (customerId != null) {
                employerService.createEmployer(customerId, employer);
            } else {
                employerService.save(employer);
            }
            redirectAttributes.addFlashAttribute("success", "Employer registered successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/employers";
    }

    @GetMapping("/edit/{id}")
    public String editEmployer(@PathVariable Long id, Model model) {
        Employer employer = employerService.findById(id)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        model.addAttribute("employer", employer);
        model.addAttribute("customers", customerService.findAll());
        return "employer-form";
    }

    @PostMapping("/update/{id}")
    public String updateEmployer(@PathVariable Long id, @ModelAttribute Employer employer,
            RedirectAttributes redirectAttributes) {
        try {
            employerService.update(id, employer);
            redirectAttributes.addFlashAttribute("success", "Employer updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/employers";
    }

    @GetMapping("/delete/{id}")
    public String deleteEmployer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            employerService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Employer deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/employers";
    }

    @GetMapping("/details/{id}")
    public String employerDetails(@PathVariable Long id, Model model) {
        Employer employer = employerService.findById(id)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        model.addAttribute("employer", employer);
        return "employer-details";
    }

    @GetMapping("/customer/{customerId}")
    public String listByCustomer(@PathVariable Long customerId, Model model) {
        List<Employer> employers = employerService.findByCustomer(customerId);
        model.addAttribute("employers", employers);
        return "employers";
    }
}