package com.aluguelcarros.aluguel_carros.repository;

import com.aluguelcarros.aluguel_carros.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    Optional<Vehicle> findByLicensePlate(String licensePlate);

    List<Vehicle> findByAvailableTrue();

    List<Vehicle> findByBrand(String brand);

    List<Vehicle> findByModel(String model);

    List<Vehicle> findByBrandAndModel(String brand, String model);

    boolean existsByLicensePlate(String licensePlate);
}
