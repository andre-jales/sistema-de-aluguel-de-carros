package com.aluguelcarros.aluguel_carros.repository;

import com.aluguelcarros.aluguel_carros.model.Order;
import com.aluguelcarros.aluguel_carros.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    List<Order> findByAgentId(Long agentId);

    List<Order> findByStatus(OrderStatus status);

    List<Order> findByCustomerIdAndStatus(Long customerId, OrderStatus status);

    List<Order> findByAgentIdAndStatus(Long agentId, OrderStatus status);
}
