package com.aluguelcarros.aluguel_carros.repository;

import com.aluguelcarros.aluguel_carros.model.CreditContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // Importe a anotação Query
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditContractRepository extends JpaRepository<CreditContract, Long> {

    @Query("SELECT cc FROM CreditContract cc JOIN FETCH cc.contract c JOIN FETCH c.order o JOIN FETCH o.customer JOIN FETCH o.vehicle")
    @Override
    List<CreditContract> findAll();

    Optional<CreditContract> findByCreditContractNumber(String creditContractNumber);

    List<CreditContract> findByApprovedTrue();

    List<CreditContract> findByApprovedFalse();

    List<CreditContract> findByBank(String bank);

    boolean existsByCreditContractNumber(String creditContractNumber);
}