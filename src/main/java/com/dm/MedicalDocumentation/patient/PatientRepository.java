package com.dm.MedicalDocumentation.patient;

import com.dm.MedicalDocumentation.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUserUserLogin (String userLogin);
    Optional<Patient> findByPersonBirthNumber (String birthNumber);

    @Query("SELECT DISTINCT me.patient FROM MedicalExamination me " +
            "WHERE me.doctor.doctorId = ?1")
    List<Patient> findDoctorsPatients(Long doctorId);

    @Query("SELECT COUNT(DISTINCT me.patient) FROM MedicalExamination me " +
            "WHERE me.doctor = ?1")
    int doctorsPatientsCount(Doctor doctor);

    @Query("SELECT COUNT(DISTINCT me.doctor) FROM MedicalExamination me " +
            "WHERE me.patient = ?1")
    int patientsDoctorsCount(Patient patient);

    @Query("SELECT DISTINCT p FROM Patient p " +
            "LEFT JOIN MedicalExamination me ON (me.patient = p) " +
            "WHERE p.generalPractitioner = ?1 " +
            "OR me.doctor = ?1")
    List<Patient> findGeneralPractitionersPatients(Doctor generalPractitioner);

    @Query("SELECT COUNT(DISTINCT p) FROM Patient p " +
            "LEFT JOIN MedicalExamination me ON (me.patient = p) " +
            "WHERE p.generalPractitioner = ?1 " +
            "OR me.doctor = ?1")
    int generalPractitionersPatientsCount(Doctor generalPractitioner);
}
