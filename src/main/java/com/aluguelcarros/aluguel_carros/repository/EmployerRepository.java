package com.aluguelcarros.aluguel_carros.repository;

import com.aluguelcarros.aluguel_carros.model.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {

    List<Employer> findByCustomerId(Long customerId);

    List<Employer> findByCompanyName(String companyName);

    List<Employer> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);
}
