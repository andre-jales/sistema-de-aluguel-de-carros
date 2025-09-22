package com.aluguelcarros.aluguel_carros.controller;

import com.aluguelcarros.aluguel_carros.model.Vehicle;
import com.aluguelcarros.aluguel_carros.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public String listVehicles(Model model) {
        List<Vehicle> vehicles = vehicleService.findAll();
        model.addAttribute("vehicles", vehicles);
        return "vehicles";
    }

    @GetMapping("/new")
    public String newVehicle(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        return "vehicle-form";
    }

    @PostMapping("/save")
    public String saveVehicle(@ModelAttribute Vehicle vehicle, RedirectAttributes redirectAttributes) {
        try {
            vehicleService.save(vehicle);
            redirectAttributes.addFlashAttribute("success", "Vehicle registered successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vehicles";
    }

    @GetMapping("/edit/{id}")
    public String editVehicle(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleService.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        model.addAttribute("vehicle", vehicle);
        return "vehicle-form";
    }

    @PostMapping("/update/{id}")
    public String updateVehicle(@PathVariable Long id, @ModelAttribute Vehicle vehicle,
            RedirectAttributes redirectAttributes) {
        try {
            vehicleService.update(id, vehicle);
            redirectAttributes.addFlashAttribute("success", "Vehicle updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vehicles";
    }

    @GetMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            vehicleService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Vehicle deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/vehicles";
    }

    @GetMapping("/details/{id}")
    public String vehicleDetails(@PathVariable Long id, Model model) {
        Vehicle vehicle = vehicleService.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        model.addAttribute("vehicle", vehicle);
        return "vehicle-details";
    }

    @GetMapping("/available")
    public String listAvailable(Model model) {
        List<Vehicle> vehicles = vehicleService.findAvailable();
        model.addAttribute("vehicles", vehicles);
        return "vehicles";
    }
}