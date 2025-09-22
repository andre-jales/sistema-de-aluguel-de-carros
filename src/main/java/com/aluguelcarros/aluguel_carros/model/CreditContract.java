package com.aluguelcarros.aluguel_carros.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contratos_credito")
public class CreditContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contract contract;

    @Column(nullable = false, unique = true)
    private String creditContractNumber;

    @Column(nullable = false)
    private String bank;

    @Column(nullable = false)
    private String agency;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private BigDecimal financedValue;

    @Column(nullable = false)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private Integer paymentTerm;

    @Column(nullable = false)
    private BigDecimal installmentValue;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(length = 2000)
    private String conditions;

    @Column(nullable = false)
    private Boolean approved;

    // Constructors
    public CreditContract() {
    }

    public CreditContract(Contract contract, String creditContractNumber,
            String bank, String agency, String account,
            BigDecimal interestRate, Integer paymentTerm,
            LocalDate dueDate, String conditions) {
        this.contract = contract;
        this.creditContractNumber = creditContractNumber;
        this.bank = bank;
        this.agency = agency;
        this.account = account;
        this.financedValue = contract.getTotalValue().subtract(contract.getDownPayment());
        this.interestRate = interestRate;
        this.paymentTerm = paymentTerm;
        this.installmentValue = this.financedValue.multiply(interestRate.add(BigDecimal.ONE))
                .divide(BigDecimal.valueOf(paymentTerm));
        this.dueDate = dueDate;
        this.creationDate = LocalDateTime.now();
        this.conditions = conditions;
        this.approved = false;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public String getCreditContractNumber() {
        return creditContractNumber;
    }

    public void setCreditContractNumber(String creditContractNumber) {
        this.creditContractNumber = creditContractNumber;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public BigDecimal getFinancedValue() {
        return financedValue;
    }

    public void setFinancedValue(BigDecimal financedValue) {
        this.financedValue = financedValue;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getPaymentTerm() {
        return paymentTerm;
    }

    public void setPaymentTerm(Integer paymentTerm) {
        this.paymentTerm = paymentTerm;
    }

    public BigDecimal getInstallmentValue() {
        return installmentValue;
    }

    public void setInstallmentValue(BigDecimal installmentValue) {
        this.installmentValue = installmentValue;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
