package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.disease.type.DiseaseType;
import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import com.dm.MedicalDocumentation.patient.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicalExaminationRepository extends JpaRepository<MedicalExamination, Long> {
    Page<MedicalExamination> findByPatientUserUserLogin(String userLogin, Pageable pageable);
    List<MedicalExamination> findByPatientAndDepartmentTypeDepartmentTypeNameAndStartTimeGreaterThan(Patient patient, String departmentTypeName, LocalDateTime starTime);

    @Query("SELECT DISTINCT me FROM MedicalExamination me " +
            "LEFT JOIN AccessRequest ar ON (ar.medicalExamination = me) " +
            "WHERE me.doctor = ?1 " +
            "OR me.patient.generalPractitioner = ?1 " +
            "OR (me.departmentType = ?2 AND me.patient IN ?3) " +
            "OR (ar.doctor = ?1 AND ar.approved) " +
            "ORDER BY me.startTime DESC")
    Page<MedicalExamination> findAllExamsWithinDepartmentAndWithAccess(Doctor doctor, DepartmentType departmentType,
                                                                       List<Patient> patients, Pageable pageable);
    @Query("SELECT DISTINCT me FROM MedicalExamination me " +
            "LEFT JOIN AccessRequest ar ON (ar.medicalExamination = me) " +
            "WHERE me.patient = ?3 " +
            "AND (me.doctor = ?1 " +
            "OR me.patient.generalPractitioner = ?1 " +
            "OR me.departmentType = ?2 " +
            "OR (ar.doctor = ?1 AND ar.approved)) " +
            "ORDER BY me.startTime DESC")
    Page<MedicalExamination> findPatientsExamsWithinDepartmentAndWithAccess(Doctor doctor, DepartmentType departmentType,
                                                                            Patient patient, Pageable pageable);

    @Query("SELECT COUNT(me), MONTH(me.startTime), YEAR(me.startTime) " +
            "FROM MedicalExamination me " +
            "WHERE me.doctor = ?1 " +
            "AND me.startTime BETWEEN ?2 AND ?3 " +
            "GROUP BY MONTH(me.startTime), YEAR(me.startTime)")
    List<Object[]> getDoctorExamCountByMonth(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(me), WEEK(me.startTime), YEAR(me.startTime) " +
            "FROM MedicalExamination me " +
            "WHERE me.doctor = ?1 " +
            "AND me.startTime BETWEEN ?2 AND ?3 " +
            "GROUP BY WEEK(me.startTime), YEAR(me.startTime)")
    List<Object[]> getDoctorExamCountByWeek(Doctor doctor, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(me), MONTH(me.startTime), YEAR(me.startTime) " +
            "FROM MedicalExamination me " +
            "WHERE me.patient = ?1 " +
            "AND me.startTime BETWEEN ?2 AND ?3 " +
            "GROUP BY MONTH(me.startTime), YEAR(me.startTime)")
    List<Object[]> getPatientsExamCountByMonth(Patient patient, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(me), WEEK(me.startTime), YEAR(me.startTime) " +
            "FROM MedicalExamination me " +
            "WHERE me.patient = ?1 " +
            "AND me.startTime BETWEEN ?2 AND ?3 " +
            "GROUP BY WEEK(me.startTime), YEAR(me.startTime)")
    List<Object[]> getPatientsExamCountByWeek(Patient patient, LocalDateTime startDate, LocalDateTime endDate);

    Long countByDoctor(Doctor doctor);
    Long countByDoctorAndDiseaseDiseaseType(Doctor doctor, DiseaseType diseaseType);
    Long countByPatient(Patient patient);
    Long countByPatientAndDepartmentType(Patient patient, DepartmentType departmentType);
}
