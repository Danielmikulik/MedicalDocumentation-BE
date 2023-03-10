package com.dm.MedicalDocumentation.patient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUserUserLogin (String userLogin);
    Optional<Patient> findByPersonBirthNumber (String userLogin);
}
