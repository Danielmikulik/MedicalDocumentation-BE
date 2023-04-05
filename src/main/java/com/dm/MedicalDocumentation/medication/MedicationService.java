package com.dm.MedicalDocumentation.medication;

import com.dm.MedicalDocumentation.util.ResultUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicationService {
    private final MedicationRepository repository;
    public boolean recordExists(String medicationName, int amount) {
        Optional<Medication> medication = repository.findByMedicationNameAndAmount(medicationName, amount);
        return medication.isPresent();
    }

    public void createMedication(MedicationRequest request) {
        Medication medication = Medication.builder()
                .medicationName(request.getName())
                .amount(request.getAmount())
                .unit(request.getUnit())
                .build();
        repository.save(medication);
    }

    public List<String> getMedications() {
        List<Medication> medications = repository.findAll();
        return ResultUtil.getMedicationsAsStringList(medications);
    }
}
