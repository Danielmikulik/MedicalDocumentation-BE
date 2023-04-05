package com.dm.MedicalDocumentation.prescription;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.medication.Medication;
import com.dm.MedicalDocumentation.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    Page<Prescription> findByPatientUserUserLoginAndMedicationMedicationNameOrderByRetrievedAtAsc(String userLogin, String medicationName, Pageable page);
    Page<Prescription> findByPatientUserUserLoginOrderByRetrievedAtAsc(String userLogin, Pageable page);
    Page<Prescription> findByRetrievedAtIsNullAndPrescribedAtGreaterThanEqual(Pageable pageable, LocalDateTime prescribedAt);

    @Query("SELECT DISTINCT m.medicationName FROM Medication m " +
            "JOIN Prescription pr ON (pr.medication = m) " +
            "WHERE pr.patient = ?1")
    List<String> findPatientsMedications(Patient patient);

    @Query("SELECT DISTINCT m FROM Medication m " +
            "JOIN Prescription pr ON (pr.medication = m) " +
            "WHERE pr.patient = ?1")
    List<Medication> findPatientsMedicationsFull(Patient patient);
    Long countByDoctor(Doctor doctor);
    Long countByDoctorAndMedication(Doctor doctor, Medication medication);
    Long countByPatient(Patient patient);
    Long countByPatientAndMedication(Patient patient, Medication medication);
    Long countByPatientAndPrescribedAtGreaterThanEqualAndRetrievedAtIsNull(Patient patient, LocalDateTime prescribedAt);
    Long countByPatientAndPrescribedAtGreaterThanEqualAndRetrievedAtIsNullAndMedication(Patient patient, LocalDateTime prescribedAt,
                                                                                        Medication medication);

    @Query("SELECT COUNT(pr) AS count, MONTH(pr.prescribedAt), YEAR(pr.prescribedAt) " +
            "FROM Prescription pr " +
            "WHERE pr.doctor = ?1 " +
            "AND pr.prescribedAt BETWEEN ?2 AND ?3 " +
            "GROUP BY MONTH(pr.prescribedAt), YEAR(pr.prescribedAt)")
    List<Object[]> getDoctorPrescriptionCountByMonth(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(pr) AS count, WEEK(pr.prescribedAt), YEAR(pr.prescribedAt) " +
            "FROM Prescription pr " +
            "WHERE pr.doctor = ?1 " +
            "AND pr.prescribedAt BETWEEN ?2 AND ?3 " +
            "GROUP BY WEEK(pr.prescribedAt), YEAR(pr.prescribedAt)")
    List<Object[]> getDoctorPrescriptionCountByWeek(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(pr) AS count, MONTH(pr.prescribedAt), YEAR(pr.prescribedAt) " +
            "FROM Prescription pr " +
            "WHERE pr.patient = ?1 " +
            "AND pr.prescribedAt BETWEEN ?2 AND ?3 " +
            "GROUP BY MONTH(pr.prescribedAt), YEAR(pr.prescribedAt)")
    List<Object[]> getPatientsPrescriptionCountByMonth(Patient patient, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(pr) AS count, WEEK(pr.prescribedAt), YEAR(pr.prescribedAt) " +
            "FROM Prescription pr " +
            "WHERE pr.patient = ?1 " +
            "AND pr.prescribedAt BETWEEN ?2 AND ?3 " +
            "GROUP BY WEEK(pr.prescribedAt), YEAR(pr.prescribedAt)")
    List<Object[]> getPatientsPrescriptionCountByWeek(Patient patient, LocalDateTime startDate, LocalDateTime endDate);
}
