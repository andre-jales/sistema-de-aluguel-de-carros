package com.aluguelcarros.aluguel_carros.service;

import com.aluguelcarros.aluguel_carros.model.Customer;
import com.aluguelcarros.aluguel_carros.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> findByCpf(String cpf) {
        return customerRepository.findByCpf(cpf);
    }

    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public Customer save(Customer customer) {
        if (customerRepository.existsByCpf(customer.getCpf())) {
            throw new RuntimeException("Já existe um cliente cadastrado com este CPF");
        }
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new RuntimeException("Já existe um cliente cadastrado com este email");
        }
        return customerRepository.save(customer);
    }

    public Customer update(Long id, Customer updatedCustomer) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Verificar se CPF já existe em outro cliente
        if (!customer.getCpf().equals(updatedCustomer.getCpf()) &&
                customerRepository.existsByCpf(updatedCustomer.getCpf())) {
            throw new RuntimeException("Já existe um cliente cadastrado com este CPF");
        }

        // Verificar se email já existe em outro cliente
        if (!customer.getEmail().equals(updatedCustomer.getEmail()) &&
                customerRepository.existsByEmail(updatedCustomer.getEmail())) {
            throw new RuntimeException("Já existe um cliente cadastrado com este email");
        }

        customer.setName(updatedCustomer.getName());
        customer.setCpf(updatedCustomer.getCpf());
        customer.setEmail(updatedCustomer.getEmail());
        customer.setPhone(updatedCustomer.getPhone());
        customer.setBirthDate(updatedCustomer.getBirthDate());
        customer.setAddress(updatedCustomer.getAddress());
        customer.setRg(updatedCustomer.getRg());
        customer.setProfession(updatedCustomer.getProfession());

        return customerRepository.save(customer);
    }

    public void delete(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado");
        }
        customerRepository.deleteById(id);
    }

    public boolean existsByCpf(String cpf) {
        return customerRepository.existsByCpf(cpf);
    }

    public boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }
}
