package com.dm.MedicalDocumentation.medication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicationRepository extends JpaRepository<Medication, Integer> {
    Optional<Medication> findByMedicationNameAndAmount(String medicationName, int amount);
    Optional<Medication> findByMedicationName(String medicationName);
}
