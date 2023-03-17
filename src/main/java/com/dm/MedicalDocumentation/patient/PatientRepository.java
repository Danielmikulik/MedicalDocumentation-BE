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

    @Query("SELECT DISTINCT p FROM Patient p " +
            "JOIN MedicalExamination me ON (me.patient = p) " +
            "WHERE p.generalPractitioner = ?1 " +
            "OR me.doctor = ?1")
    List<Patient> findGeneralPractitionersPatients(Doctor generalPractitioner);
}
