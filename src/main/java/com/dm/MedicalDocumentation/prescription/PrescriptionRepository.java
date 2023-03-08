package com.dm.MedicalDocumentation.prescription;

import com.dm.MedicalDocumentation.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatientUserUserLoginAndMedicationMedicationNameOrderByRetrievedAtAsc(String userLogin, String medicationName);

    @Query("SELECT DISTINCT m.medicationName FROM Medication m " +
            "JOIN Prescription pr ON (pr.medication = m) " +
            "WHERE pr.patient = ?1")
    List<String> findPatientsMedications(Patient patient);
}
