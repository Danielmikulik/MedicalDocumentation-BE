package com.dm.MedicalDocumentation.prescription;

import com.dm.MedicalDocumentation.patient.Patient;
import com.dm.MedicalDocumentation.patient.PatientRepository;
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
    private final PatientRepository patientRepository;

    public List<PrescriptionResponse> getPatientsPrescriptions(String userLogin, String medication) {
        List<Prescription> prescriptions = repository
                .findByPatientUserUserLoginAndMedicationMedicationNameOrderByRetrievedAtAsc(userLogin, medication);
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
                            .doctorId(prescription.getDoctor().getDoctorId())
                            .prescribedAt(prescription.getPrescribedAt())
                            .retrievedAt(retrievedAt)
                    .build());
        }

        return result;
    }

    public List<String> getPatientsMedications(String userLogin) {
        Patient patient = patientRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("No patient with userLogin" + userLogin + "exists."));
        List<String> medications = repository.findPatientsMedications(patient);

        return medications;
    }
}
