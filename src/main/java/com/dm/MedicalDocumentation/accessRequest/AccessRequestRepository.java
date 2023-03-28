package com.dm.MedicalDocumentation.accessRequest;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.medicalExamination.MedicalExamination;
import com.dm.MedicalDocumentation.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> {
    List<AccessRequest> findByMedicalExaminationMedicalExaminationId(Long medicalExamId);
    Optional<AccessRequest> findByMedicalExaminationAndDoctorAndRejected(
            MedicalExamination medicalExamination, Doctor doctor, boolean rejected);

    @Query("SELECT count(*), me.patient, me.departmentType, ar.rejected FROM AccessRequest ar " +
            "JOIN MedicalExamination me ON (ar.medicalExamination = me) " +
            "WHERE ar.doctor = ?1 AND ar.approved = false " +
            "GROUP BY me.patient, me.departmentType, ar.rejected")
    List<Object[]> getNonApprovedAccessRequestCounts(Doctor doctor);

    @Query("SELECT DISTINCT ar " +
            "FROM AccessRequest ar " +
            "JOIN MedicalExamination me ON (ar.medicalExamination = me) " +
            "WHERE me.patient.generalPractitioner = ?1 " +
            "AND ar.approved = false " +
            "AND ar.rejected = false " +
            "AND me.patient.person.name || ' ' || me.patient.person.surname LIKE %?2% " +
            "AND me.patient.person.birthNumber LIKE %?3% " +
            "AND ar.doctor.person.name || ' ' || ar.doctor.person.surname LIKE %?4% " +
            "AND ar.medicalExamination.doctor.person.name || ' ' || ar.medicalExamination.doctor.person.surname LIKE %?5% " +
            "AND ar.medicalExamination.departmentType.departmentTypeName LIKE  %?6%")
    Page<AccessRequest> getGeneralPractitionersPatientsNonRejectedAccessRequests(Doctor doctor, String patientName, String birthNumber,
                                                                                 String requestDoctor, String examDoctor, String department,
                                                                                 Pageable pageable);

    @Query("SELECT DISTINCT ar " +
            "FROM AccessRequest ar " +
            "JOIN MedicalExamination me ON (ar.medicalExamination = me) " +
            "WHERE me.patient.generalPractitioner = ?1 " +
            "AND ar.approved = false " +
            "AND me.patient.person.name || ' ' || me.patient.person.surname LIKE %?2% " +
            "AND me.patient.person.birthNumber LIKE %?3% " +
            "AND ar.doctor.person.name || ' ' || ar.doctor.person.surname LIKE %?4% " +
            "AND ar.medicalExamination.doctor.person.name || ' ' || ar.medicalExamination.doctor.person.surname LIKE %?5% " +
            "AND ar.medicalExamination.departmentType.departmentTypeName LIKE  %?6%")
    Page<AccessRequest> getGeneralPractitionersPatientsAllAccessRequests(Doctor doctor, String patientName, String birthNumber,
                                                                         String requestDoctor, String examDoctor, String department,
                                                                         Pageable pageable);

    @Query("SELECT DISTINCT ar " +
            "FROM AccessRequest ar " +
            "JOIN MedicalExamination me ON (ar.medicalExamination = me) " +
            "WHERE me.patient.generalPractitioner = ?1 " +
            "AND ar.approved = false " +
            "AND ar.rejected = true " +
            "AND me.patient.person.name || ' ' || me.patient.person.surname LIKE %?2% " +
            "AND me.patient.person.birthNumber LIKE %?3% " +
            "AND ar.doctor.person.name || ' ' || ar.doctor.person.surname LIKE %?4% " +
            "AND ar.medicalExamination.doctor.person.name || ' ' || ar.medicalExamination.doctor.person.surname LIKE %?5% " +
            "AND ar.medicalExamination.departmentType.departmentTypeName LIKE  %?6%")
    Page<AccessRequest> getGeneralPractitionersPatientsRejectedAccessRequests(Doctor doctor, String patientName, String birthNumber,
                                                                              String requestDoctor, String examDoctor, String department,
                                                                              Pageable pageable);

    @Query("SELECT DISTINCT p FROM MedicalExamination me " +
            "JOIN AccessRequest ar ON (ar.medicalExamination = me) " +
            "JOIN Patient p ON (me.patient = p)" +
            "WHERE p.generalPractitioner = ?1 AND ar.approved = false")
    List<Patient> getGeneralPractitionersPatientsWithDisapprovedAccessRequests(Doctor doctor);
}
