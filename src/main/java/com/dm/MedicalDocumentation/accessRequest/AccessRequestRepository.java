package com.dm.MedicalDocumentation.accessRequest;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.medicalExamination.MedicalExamination;
import com.dm.MedicalDocumentation.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> {
    List<AccessRequest> findByMedicalExaminationMedicalExaminationId(Long medicalExamId);
    Optional<AccessRequest> findByMedicalExaminationAndPatientAndDoctor(
            MedicalExamination medicalExamination, Patient patient, Doctor doctor);

    @Query("SELECT count(*), ar.patient, me.departmentType FROM AccessRequest ar " +
            "JOIN MedicalExamination me ON (ar.medicalExamination = me) " +
            "WHERE ar.doctor = ?1 AND ar.approved = false " +
            "GROUP BY ar.patient, me.departmentType")
    List<Object[]> getAccessRequestCounts(Doctor doctor);

    @Query("SELECT DISTINCT ar " +
            "FROM AccessRequest ar " +
            "WHERE ar.patient.generalPractitioner = ?1 " +
            "AND ar.approved = false " +
            "AND ar.patient.person.name || ar.patient.person.surname LIKE %?2% " +
            "AND ar.patient.person.birthNumber LIKE %?3% " +
            "AND ar.doctor.person.name || ar.doctor.person.surname LIKE %?4% " +
            "AND ar.medicalExamination.doctor.person.name || ar.medicalExamination.doctor.person.surname LIKE %?5% " +
            "AND ar.medicalExamination.departmentType.departmentTypeName LIKE  %?6%")
    Page<AccessRequest> getGeneralPractitionersPatientsAccessRequests(Doctor doctor, String patientName, String birthNumber,
                                                                              String requestDoctor, String examDoctor, String department,
                                                                              Pageable pageable);

    @Query("SELECT DISTINCT p FROM AccessRequest ar " +
            "JOIN Patient p ON (ar.patient = p)" +
            "WHERE p.generalPractitioner = ?1 AND ar.approved = false")
    List<Patient> getGeneralPractitionersPatientsWithDisapprovedAccessRequests(Doctor doctor);
}
