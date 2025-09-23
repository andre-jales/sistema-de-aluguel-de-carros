package com.aluguelcarros.aluguel_carros.service;

import com.aluguelcarros.aluguel_carros.model.Vehicle;
import com.aluguelcarros.aluguel_carros.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    public Optional<Vehicle> findById(Long id) {
        return vehicleRepository.findById(id);
    }

    public Optional<Vehicle> findByLicensePlate(String licensePlate) {
        return vehicleRepository.findByLicensePlate(licensePlate);
    }

    public List<Vehicle> findAvailable() {
        return vehicleRepository.findByAvailableTrue();
    }

    public List<Vehicle> findByBrand(String brand) {
        return vehicleRepository.findByBrand(brand);
    }

    public List<Vehicle> findByModel(String model) {
        return vehicleRepository.findByModel(model);
    }

    public List<Vehicle> findByBrandAndModel(String brand, String model) {
        return vehicleRepository.findByBrandAndModel(brand, model);
    }

    public Vehicle save(Vehicle vehicle) {
        if (vehicleRepository.existsByLicensePlate(vehicle.getLicensePlate())) {
            throw new RuntimeException("Já existe um automóvel cadastrado com esta placa");
        }
        return vehicleRepository.save(vehicle);
    }

    public Vehicle update(Long id, Vehicle updatedVehicle) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Automóvel não encontrado"));

        if (!vehicle.getLicensePlate().equals(updatedVehicle.getLicensePlate()) &&
                vehicleRepository.existsByLicensePlate(updatedVehicle.getLicensePlate())) {
            throw new RuntimeException("Já existe um automóvel cadastrado com esta placa");
        }

        vehicle.setBrand(updatedVehicle.getBrand());
        vehicle.setModel(updatedVehicle.getModel());
        vehicle.setLicensePlate(updatedVehicle.getLicensePlate());
        vehicle.setYear(updatedVehicle.getYear());
        vehicle.setColor(updatedVehicle.getColor());
        vehicle.setDailyValue(updatedVehicle.getDailyValue());
        vehicle.setAvailable(updatedVehicle.getAvailable());
        vehicle.setDescription(updatedVehicle.getDescription());

        return vehicleRepository.save(vehicle);
    }

    public void delete(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new RuntimeException("Automóvel não encontrado");
        }
        vehicleRepository.deleteById(id);
    }

    public boolean existsByLicensePlate(String licensePlate) {
        return vehicleRepository.existsByLicensePlate(licensePlate);
    }

    public Vehicle changeAvailability(Long id, Boolean available) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Automóvel não encontrado"));

        vehicle.setAvailable(available);
        return vehicleRepository.save(vehicle);
    }
}
