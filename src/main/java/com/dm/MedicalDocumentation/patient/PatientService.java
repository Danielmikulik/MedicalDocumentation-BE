package com.dm.MedicalDocumentation.patient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository repository;

    public String getPatientNameByBirthNumber(String birthNumber) {
        Optional<Patient> patient = repository.findByPersonBirthNumber(birthNumber);
        return patient.map(value -> value.getPerson().getFullName()).orElse(null);
    }
}
