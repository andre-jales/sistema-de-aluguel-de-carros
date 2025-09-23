package com.aluguelcarros.aluguel_carros.service;

import com.aluguelcarros.aluguel_carros.model.*;
import com.aluguelcarros.aluguel_carros.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private CreditContractRepository creditContractRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Contract> findAll() {
        return contractRepository.findAll();
    }

    public Optional<Contract> findById(Long id) {
        return contractRepository.findById(id);
    }

    public Optional<Contract> findByContractNumber(String contractNumber) {
        return contractRepository.findByContractNumber(contractNumber);
    }

    public List<Contract> findByAgent(Long agentId) {
        return contractRepository.findByAgentId(agentId);
    }

    public List<Contract> findActive() {
        return contractRepository.findByActiveTrue();
    }

    public Contract generateContract(Long orderId, Long agentId, BigDecimal downPayment,
            Integer installments, String terms) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new RuntimeException("Only approved orders can generate contracts");
        }

        if (order.getContract() != null) {
            throw new RuntimeException("Order already has a generated contract");
        }

        String contractNumber = "CTR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Contract contract = new Contract(order, agent, contractNumber, downPayment, installments, terms);
        contract = contractRepository.save(contract);

        order.setStatus(OrderStatus.CONTRACT_GENERATED);
        order.setUpdateDate(LocalDateTime.now());
        orderRepository.save(order);

        return contract;
    }

    public CreditContract generateCreditContract(Long contractId, String bank, String branch,
            String account, BigDecimal interestRate,
            Integer paymentTerm, String conditions) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        if (contract.getCreditContract() != null) {
            throw new RuntimeException("Contract already has a credit contract");
        }

        String creditContractNumber = "CCR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        LocalDate dueDate = LocalDate.now().plusMonths(paymentTerm);

        CreditContract creditContract = new CreditContract(contract, creditContractNumber,
                bank, branch, account, interestRate,
                paymentTerm, dueDate, conditions);

        return creditContractRepository.save(creditContract);
    }

    public CreditContract approveCreditContract(Long creditContractId) {
        CreditContract creditContract = creditContractRepository.findById(creditContractId)
                .orElseThrow(() -> new RuntimeException("Credit contract not found"));

        creditContract.setApproved(true);
        return creditContractRepository.save(creditContract);
    }

    public CreditContract rejectCreditContract(Long creditContractId) {
        CreditContract creditContract = creditContractRepository.findById(creditContractId)
                .orElseThrow(() -> new RuntimeException("Credit contract not found"));

        creditContract.setApproved(false);
        return creditContractRepository.save(creditContract);
    }

    public Contract updateContract(Long id, Contract updatedContract) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        contract.setDownPayment(updatedContract.getDownPayment());
        contract.setInstallmentsQuantity(updatedContract.getInstallmentsQuantity());
        contract.setTerms(updatedContract.getTerms());

        BigDecimal installmentValue = contract.getTotalValue()
                .subtract(contract.getDownPayment())
                .divide(BigDecimal.valueOf(contract.getInstallmentsQuantity()));
        contract.setInstallmentValue(installmentValue);

        return contractRepository.save(contract);
    }

    public void cancelContract(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));

        contract.setActive(false);
        contractRepository.save(contract);

        Order order = contract.getOrder();
        Vehicle vehicle = order.getVehicle();
        vehicle.setAvailable(true);
        vehicleRepository.save(vehicle);
    }

    public List<CreditContract> listCreditContracts() {
        return creditContractRepository.findAll();
    }

    public List<CreditContract> findApprovedCreditContracts() {
        return creditContractRepository.findByApprovedTrue();
    }

    public List<CreditContract> findPendingCreditContracts() {
        return creditContractRepository.findByApprovedFalse();
    }
}