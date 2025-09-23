package com.aluguelcarros.aluguel_carros.repository;

import com.aluguelcarros.aluguel_carros.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    @Query("SELECT c FROM Contract c JOIN FETCH c.order o JOIN FETCH o.customer JOIN FETCH o.vehicle")
    @Override
    List<Contract> findAll();

    Optional<Contract> findByContractNumber(String contractNumber);

    List<Contract> findByAgentId(Long agentId);

    List<Contract> findByActiveTrue();

    List<Contract> findByActiveFalse();

    boolean existsByContractNumber(String contractNumber);
}