package com.aluguelcarros.aluguel_carros.service;

import com.aluguelcarros.aluguel_carros.model.*;
import com.aluguelcarros.aluguel_carros.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AgentRepository agentRepository;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> findByAgent(Long agentId) {
        return orderRepository.findByAgentId(agentId);
    }

    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    public Order createOrder(Long customerId, Long vehicleId, Order order) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if (!vehicle.getAvailable()) {
            throw new RuntimeException("Car is not available for rental");
        }

        order.setCustomer(customer);
        order.setVehicle(vehicle);
        order.setStatus(OrderStatus.PENDING);
        order.setCreationDate(LocalDateTime.now());

        int days = (int) java.time.temporal.ChronoUnit.DAYS.between(order.getStartDate(), order.getEndDate());
        order.setDaysQuantity(days);
        order.setTotalValue(vehicle.getDailyValue().multiply(java.math.BigDecimal.valueOf(days)));

        return orderRepository.save(order);
    }

    public Order assignAgent(Long orderId, Long agentId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        order.setAgent(agent);
        order.setStatus(OrderStatus.EVALUATING);
        order.setCreationDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public Order approveOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.EVALUATING) {
            throw new RuntimeException("Order must be under review to be approved");
        }

        order.setStatus(OrderStatus.APPROVED);
        order.setCreationDate(LocalDateTime.now());

        Vehicle vehicle = order.getVehicle();
        vehicle.setAvailable(false);
        vehicleRepository.save(vehicle);

        return orderRepository.save(order);
    }

    public Order rejectOrder(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.EVALUATING) {
            throw new RuntimeException("Order must be under review to be rejected");
        }

        order.setStatus(OrderStatus.REJECTED);
        order.setObservations(order.getObservations() + "\nRejection reason: " + reason);
        order.setUpdateDate(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == OrderStatus.CONTRACT_GENERATED) {
            throw new RuntimeException("It is not possible to cancel an order with a generated contract");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setUpdateDate(LocalDateTime.now());

        if (order.getStatus() == OrderStatus.APPROVED) {
            Vehicle vehicle = order.getVehicle();
            vehicle.setAvailable(true);
            vehicleRepository.save(vehicle);
        }

        return orderRepository.save(order);
    }

    public Order updateOrder(Long id, Order updatedOrder) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only pending orders can be updated");
        }

        order.setStartDate(updatedOrder.getStartDate());
        order.setEndDate(updatedOrder.getEndDate());
        order.setObservations(updatedOrder.getObservations());
        order.setUpdateDate(LocalDateTime.now());

        int days = (int) java.time.temporal.ChronoUnit.DAYS.between(order.getStartDate(), order.getEndDate());
        order.setDaysQuantity(days);
        order.setTotalValue(order.getVehicle().getDailyValue().multiply(java.math.BigDecimal.valueOf(days)));

        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Only pending orders can be deleted");
        }

        orderRepository.deleteById(id);
    }
}
