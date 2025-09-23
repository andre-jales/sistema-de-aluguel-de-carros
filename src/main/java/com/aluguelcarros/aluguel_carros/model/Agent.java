package com.aluguelcarros.aluguel_carros.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "agent")
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String position;

    @Column(nullable = false)
    private String department;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> evaluatedOrders;

    @OneToMany(mappedBy = "agent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Contract> generatedContracts;

    // Constructors
    public Agent() {
    }

    public Agent(String name, String cpf, String email, String phone,
            String position, String department) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
        this.position = position;
        this.department = department;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Order> getEvaluatedOrders() {
        return evaluatedOrders;
    }

    public void setEvaluatedOrders(List<Order> evaluatedOrders) {
        this.evaluatedOrders = evaluatedOrders;
    }

    public List<Contract> getGeneratedContracts() {
        return generatedContracts;
    }

    public void setGeneratedContracts(List<Contract> generatedContracts) {
        this.generatedContracts = generatedContracts;
    }
}
