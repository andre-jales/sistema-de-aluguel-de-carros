package com.aluguelcarros.aluguel_carros.service;

import com.aluguelcarros.aluguel_carros.model.Employer;
import com.aluguelcarros.aluguel_carros.model.Customer;
import com.aluguelcarros.aluguel_carros.repository.EmployerRepository;
import com.aluguelcarros.aluguel_carros.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployerService {

    @Autowired
    private EmployerRepository employerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<Employer> findAll() {
        return employerRepository.findAll();
    }

    public Optional<Employer> findById(Long id) {
        return employerRepository.findById(id);
    }

    public List<Employer> findByCustomer(Long customerId) {
        return employerRepository.findByCustomerId(customerId);
    }

    public List<Employer> findByCompanyName(String companyName) {
        return employerRepository.findByCompanyName(companyName);
    }

    public List<Employer> findByCnpj(String cnpj) {
        return employerRepository.findByCnpj(cnpj);
    }

    public Employer save(Employer employer) {
        if (employerRepository.existsByCnpj(employer.getCnpj())) {
            throw new RuntimeException("An employer with this CNPJ already exists");
        }
        return employerRepository.save(employer);
    }

    public Employer update(Long id, Employer updatedEmployer) {
        Employer employer = employerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employer not found"));

        if (!employer.getCnpj().equals(updatedEmployer.getCnpj()) &&
                employerRepository.existsByCnpj(updatedEmployer.getCnpj())) {
            throw new RuntimeException("An employer with this CNPJ already exists");
        }

        employer.setCompanyName(updatedEmployer.getCompanyName());
        employer.setPosition(updatedEmployer.getPosition());
        employer.setIncome(updatedEmployer.getIncome());
        employer.setPhone(updatedEmployer.getPhone());
        employer.setAddress(updatedEmployer.getAddress());
        employer.setCnpj(updatedEmployer.getCnpj());

        return employerRepository.save(employer);
    }

    public void delete(Long id) {
        if (!employerRepository.existsById(id)) {
            throw new RuntimeException("Employer not found");
        }
        employerRepository.deleteById(id);
    }

    public boolean existsByCnpj(String cnpj) {
        return employerRepository.existsByCnpj(cnpj);
    }

    public Employer createEmployer(Long customerId, Employer employer) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        employer.setCustomer(customer);
        return save(employer);
    }
}
