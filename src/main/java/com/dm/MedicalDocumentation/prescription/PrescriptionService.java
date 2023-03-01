package com.dm.MedicalDocumentation.prescription;

import com.dm.MedicalDocumentation.request.UserLoginRequest;
import com.dm.MedicalDocumentation.response.PrescriptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

    private final PrescriptionRepository repository;

    public List<PrescriptionResponse> getPatientsPrescriptions(String userLogin) {
        List<Prescription> prescriptions = repository.findByPatientUserUserLogin(userLogin);
        List<PrescriptionResponse> result = new ArrayList<>(prescriptions.size());

        for (Prescription prescription : prescriptions) {
            LocalDateTime retrievedAt = prescription.getRetrievedAt() == null
                    ? LocalDateTime.of(0, 1, 1, 0, 0)
                    : prescription.getRetrievedAt();
            result.add(PrescriptionResponse.builder()
                            .medicationName(prescription.getMedication().getMedicationName())
                            .medicationAmount(prescription.getMedication().getAmount() + " " + prescription.getMedication().getUnit())
                            .packageCount(prescription.getAmount())
                            .doctor(prescription.getDoctor().getPerson().getFullName())
                            .prescribedAt(prescription.getPrescribedAt())
                            .retrievedAt(retrievedAt)
                    .build());
        }

        return result;
    }
}
