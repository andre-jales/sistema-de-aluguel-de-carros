package com.aluguelcarros.aluguel_carros.repository;

import com.aluguelcarros.aluguel_carros.model.CreditContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditContractRepository extends JpaRepository<CreditContract, Long> {

    Optional<CreditContract> findByCreditContractNumber(String creditContractNumber);

    List<CreditContract> findByApprovedTrue();

    List<CreditContract> findByApprovedFalse();

    List<CreditContract> findByBank(String bank);

    boolean existsByCreditContractNumber(String creditContractNumber);
}
