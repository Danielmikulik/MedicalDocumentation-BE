package com.dm.MedicalDocumentation.healthInsurance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HealthInsuranceService {
    private final HealthInsuranceRepository repository;
    public boolean recordExists(String name) {
        Optional<HealthInsurance> healthInsurance = repository.findByInsuranceName(name);
        return healthInsurance.isPresent();
    }

    public void createHealthInsurance(String name) {
        HealthInsurance healthInsurance = HealthInsurance.builder()
                .insuranceName(name)
                .build();
        repository.save(healthInsurance);
    }

    public List<String> getHealthInsurances() {
        List<HealthInsurance> healthInsurances = repository.findAll();
        List<String> result = new ArrayList<>(healthInsurances.size());
        for (HealthInsurance healthInsurance : healthInsurances) {
            result.add(healthInsurance.getInsuranceName());
        }
        return result;
    }
}
