package com.aluguelcarros.aluguel_carros.controller;

import com.aluguelcarros.aluguel_carros.model.Agent;
import com.aluguelcarros.aluguel_carros.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/agents")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @GetMapping
    public String listAgents(Model model) {
        List<Agent> agents = agentService.findAll();
        model.addAttribute("agents", agents);
        return "agents";
    }

    @GetMapping("/new")
    public String newAgent(Model model) {
        model.addAttribute("agent", new Agent());
        return "agent-form";
    }

    @PostMapping("/save")
    public String saveAgent(@ModelAttribute Agent agent, RedirectAttributes redirectAttributes) {
        try {
            agentService.save(agent);
            redirectAttributes.addFlashAttribute("success", "Agent registered successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/agents";
    }

    @GetMapping("/edit/{id}")
    public String editAgent(@PathVariable Long id, Model model) {
        Agent agent = agentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        model.addAttribute("agent", agent);
        return "agent-form";
    }

    @PostMapping("/update/{id}")
    public String updateAgent(@PathVariable Long id, @ModelAttribute Agent agent,
            RedirectAttributes redirectAttributes) {
        try {
            agentService.update(id, agent);
            redirectAttributes.addFlashAttribute("success", "Agent updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/agents";
    }

    @GetMapping("/delete/{id}")
    public String deleteAgent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            agentService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Agent deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/agents";
    }

    @GetMapping("/details/{id}")
    public String agentDetails(@PathVariable Long id, Model model) {
        Agent agent = agentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Agent not found"));
        model.addAttribute("agent", agent);
        return "agent-details";
    }
}