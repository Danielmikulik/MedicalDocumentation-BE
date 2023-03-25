package com.dm.MedicalDocumentation.prescription;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatientUserUserLoginAndMedicationMedicationNameOrderByRetrievedAtAsc(String userLogin, String medicationName);

    @Query("SELECT DISTINCT m.medicationName FROM Medication m " +
            "JOIN Prescription pr ON (pr.medication = m) " +
            "WHERE pr.patient = ?1")
    List<String> findPatientsMedications(Patient patient);
    Long countByDoctor(Doctor doctor);
    Long countByPatient(Patient patient);
    Long countByPatientAndPrescribedAtGreaterThanEqualAndRetrievedAtIsNull(Patient patient, LocalDateTime prescribedAt);

    @Query("SELECT COUNT(pr) AS count, MONTH(pr.prescribedAt) AS month " +
            "FROM Prescription pr " +
            "WHERE pr.doctor = ?1 " +
            "AND pr.prescribedAt BETWEEN ?2 AND ?3 " +
            "GROUP BY MONTH(pr.prescribedAt)")
    List<Object[]> getDoctorPrescriptionCountByMonth(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(pr) AS count, MONTH(pr.prescribedAt) AS month " +
            "FROM Prescription pr " +
            "WHERE pr.patient = ?1 " +
            "AND pr.prescribedAt BETWEEN ?2 AND ?3 " +
            "GROUP BY MONTH(pr.prescribedAt)")
    List<Object[]> getPatientsPrescriptionCountByMonth(Patient patient, LocalDateTime startDate, LocalDateTime endDate);
}
