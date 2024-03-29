package com.dm.MedicalDocumentation.patient;

import com.dm.MedicalDocumentation.disease.type.DiseaseType;
import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByUserUserLogin (String userLogin);
    Optional<Patient> findByPersonBirthNumber (String birthNumber);

    @Query("SELECT DISTINCT me.patient FROM MedicalExamination me " +
            "WHERE me.doctor = ?1")
    List<Patient> findDoctorsPatients(Doctor doctor);

    @Query("SELECT DISTINCT me.patient FROM MedicalExamination me " +
            "LEFT JOIN AccessRequest ar ON (ar.medicalExamination = me) " +
            "WHERE me.doctor = ?1 " +
            "OR (ar.doctor = ?1 AND ar.approved = true AND ar.rejected = false)")
    List<Patient> findDoctorsPatientsIncludingAR(Doctor doctor);

    @Query("SELECT COUNT(DISTINCT me.doctor) FROM MedicalExamination me " +
            "WHERE me.patient = ?1")
    int patientsDoctorsCount(Patient patient);

    @Query("SELECT COUNT(DISTINCT me.doctor) FROM MedicalExamination me " +
            "WHERE me.patient = ?1 " +
            "AND me.departmentType = ?2")
    int patientsDoctorsCountByDepartmentType(Patient patient, DepartmentType departmentType);

    @Query("SELECT DISTINCT p FROM Patient p " +
            "LEFT JOIN MedicalExamination me ON (me.patient = p) " +
            "WHERE p.generalPractitioner = ?1 " +
            "OR me.doctor = ?1")
    List<Patient> findGeneralPractitionersPatients(Doctor generalPractitioner);

    @Query("SELECT DISTINCT p FROM Patient p " +
            "LEFT JOIN MedicalExamination me ON (me.patient = p) " +
            "LEFT JOIN AccessRequest ar ON (ar.medicalExamination = me) " +
            "WHERE p.generalPractitioner = ?1 " +
            "OR me.doctor = ?1 " +
            "OR (ar.doctor = ?1 AND ar.approved = true AND ar.rejected = false)")
    List<Patient> findGeneralPractitionersPatientsIncludingAR(Doctor generalPractitioner);

    @Query("SELECT COUNT(DISTINCT me.patient) FROM MedicalExamination me " +
            "WHERE me.doctor = ?1")
    int doctorsPatientsCount(Doctor doctor);

    @Query("SELECT COUNT(DISTINCT p) FROM Patient p " +
            "LEFT JOIN MedicalExamination me ON (me.patient = p) " +
            "WHERE (p.generalPractitioner = ?1 OR me.doctor = ?1)")
    int generalPractitionersPatientsCount(Doctor generalPractitioner);

    @Query("SELECT COUNT(DISTINCT me.patient) FROM MedicalExamination me " +
            "JOIN Disease d ON (d.patient = me.patient) " +
            "WHERE me.doctor = ?1 " +
            "AND d.diseaseType = ?2 " +
            "AND d.diagnosed <= me.endTime " +
            "AND (d.cured IS NULL OR d.cured >= me.startTime)")
    int doctorsPatientsCountWithDisease(Doctor doctor, DiseaseType diseaseType);

    @Query("SELECT COUNT(DISTINCT p) FROM Patient p " +
            "LEFT JOIN MedicalExamination me ON (me.patient = p) " +
            "JOIN Disease d ON (d.patient = me.patient) " +
            "WHERE (p.generalPractitioner = ?1 OR me.doctor = ?1) " +
            "AND d.diseaseType = ?2 " +
            "AND d.diagnosed <= me.endTime " +
            "AND (d.cured IS NULL OR d.cured >= me.startTime)")
    int generalPractitionersPatientsCountWithDisease(Doctor generalPractitioner, DiseaseType diseaseType);
}
