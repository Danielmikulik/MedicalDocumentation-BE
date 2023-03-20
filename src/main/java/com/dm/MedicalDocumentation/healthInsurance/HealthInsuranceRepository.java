package com.dm.MedicalDocumentation.healthInsurance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthInsuranceRepository extends JpaRepository<HealthInsurance, Integer> {
    Optional<HealthInsurance> findByInsuranceName(String hospitalName);
}
