
package com.aluguelcarros.aluguel_carros.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aluguelcarros.aluguel_carros.model.Agent;
import com.aluguelcarros.aluguel_carros.repository.AgentRepository;

@Service
public class AgentService {

    @Autowired
    private AgentRepository agentRepository;

    public List<Agent> findAll() {
        return agentRepository.findAll();
    }

    public Optional<Agent> findById(Long id) {
        return agentRepository.findById(id);
    }

    public Optional<Agent> findByCpf(String cpf) {
        return agentRepository.findByCpf(cpf);
    }

    public Optional<Agent> findByEmail(String email) {
        return agentRepository.findByEmail(email);
    }

    public Agent save(Agent agent) {
        if (agentRepository.existsByCpf(agent.getCpf())) {
            throw new RuntimeException("An agent with this CPF already exists");
        }
        if (agentRepository.existsByEmail(agent.getEmail())) {
            throw new RuntimeException("An agent with this email already exists");
        }
        return agentRepository.save(agent);
    }

    public Agent update(Long id, Agent updatedAgent) {
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        if (!agent.getCpf().equals(updatedAgent.getCpf()) &&
                agentRepository.existsByCpf(updatedAgent.getCpf())) {
            throw new RuntimeException("An agent with this CPF already exists");
        }

        if (!agent.getEmail().equals(updatedAgent.getEmail()) &&
                agentRepository.existsByEmail(updatedAgent.getEmail())) {
            throw new RuntimeException("An agent with this email already exists");
        }

        agent.setName(updatedAgent.getName());
        agent.setCpf(updatedAgent.getCpf());
        agent.setEmail(updatedAgent.getEmail());
        agent.setPhone(updatedAgent.getPhone());
        agent.setPosition(updatedAgent.getPosition());
        agent.setDepartment(updatedAgent.getDepartment());

        return agentRepository.save(agent);
    }

    public void delete(Long id) {
        if (!agentRepository.existsById(id)) {
            throw new RuntimeException("Agent not found");
        }
        agentRepository.deleteById(id);
    }

    public boolean existsByCpf(String cpf) {
        return agentRepository.existsByCpf(cpf);
    }

    public boolean existsByEmail(String email) {
        return agentRepository.existsByEmail(email);
    }
}
