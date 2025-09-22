package com.aluguelcarros.aluguel_carros.controller;

import com.aluguelcarros.aluguel_carros.model.*;
import com.aluguelcarros.aluguel_carros.service.ContractService;
import com.aluguelcarros.aluguel_carros.service.OrderService;
import com.aluguelcarros.aluguel_carros.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/contracts")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AgentService agentService;

    @GetMapping
    public String listContracts(Model model) {
        List<Contract> contracts = contractService.findAll();
        model.addAttribute("contracts", contracts);
        return "contracts";
    }

    @GetMapping("/generate/{orderId}")
    public String generateContract(@PathVariable Long orderId, Model model) {
        Order order = orderService.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        model.addAttribute("order", order);
        model.addAttribute("agents", agentService.findAll());
        return "generate-contract";
    }

    @PostMapping("/generate/{orderId}")
    public String generateContract(@PathVariable Long orderId,
            @RequestParam Long agentId,
            @RequestParam BigDecimal downPayment,
            @RequestParam Integer installments,
            @RequestParam String terms,
            RedirectAttributes redirectAttributes) {
        try {
            contractService.generateContract(orderId, agentId, downPayment, installments, terms);
            redirectAttributes.addFlashAttribute("success", "Contract generated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/contracts";
    }

    @GetMapping("/credit/{contractId}")
    public String generateCreditContract(@PathVariable Long contractId, Model model) {
        Contract contract = contractService.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
        model.addAttribute("contract", contract);
        return "generate-credit-contract";
    }

    @PostMapping("/credit/{contractId}")
    public String generateCreditContract(@PathVariable Long contractId,
            @RequestParam String bank,
            @RequestParam String branch,
            @RequestParam String account,
            @RequestParam BigDecimal interestRate,
            @RequestParam Integer paymentTerm,
            @RequestParam String conditions,
            RedirectAttributes redirectAttributes) {
        try {
            contractService.generateCreditContract(contractId, bank, branch, account,
                    interestRate, paymentTerm, conditions);
            redirectAttributes.addFlashAttribute("success", "Credit contract generated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/contracts";
    }

    @GetMapping("/credit/approve/{id}")
    public String approveCreditContract(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            contractService.approveCreditContract(id);
            redirectAttributes.addFlashAttribute("success", "Credit contract approved successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/contracts/credit";
    }

    @GetMapping("/credit/reject/{id}")
    public String rejectCreditContract(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            contractService.rejectCreditContract(id);
            redirectAttributes.addFlashAttribute("success", "Credit contract rejected successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/contracts/credit";
    }

    @GetMapping("/credit")
    public String listCreditContracts(Model model) {
        List<CreditContract> creditContracts = contractService.listCreditContracts();
        model.addAttribute("creditContracts", creditContracts);
        return "credit-contracts";
    }

    @GetMapping("/edit/{id}")
    public String editContract(@PathVariable Long id, Model model) {
        Contract contract = contractService.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
        model.addAttribute("contract", contract);
        return "edit-contract";
    }

    @PostMapping("/update/{id}")
    public String updateContract(@PathVariable Long id, @ModelAttribute Contract contract,
            RedirectAttributes redirectAttributes) {
        try {
            contractService.updateContract(id, contract);
            redirectAttributes.addFlashAttribute("success", "Contract updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/contracts";
    }

    @GetMapping("/cancel/{id}")
    public String cancelContract(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            contractService.cancelContract(id);
            redirectAttributes.addFlashAttribute("success", "Contract canceled successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/contracts";
    }

    @GetMapping("/details/{id}")
    public String contractDetails(@PathVariable Long id, Model model) {
        Contract contract = contractService.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
        model.addAttribute("contract", contract);
        return "contract-details";
    }
}