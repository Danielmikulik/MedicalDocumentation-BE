package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import com.dm.MedicalDocumentation.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicalExaminationRepository extends JpaRepository<MedicalExamination, Long> {
    List<MedicalExamination> findByPatientUserUserLogin(String userLogin);
    List<MedicalExamination> findByDoctorUserUserLogin(String userLogin);
    List<MedicalExamination> findByPatientAndDepartmentTypeDepartmentTypeNameAndStartTimeGreaterThan(Patient patient, String departmentTypeName, LocalDateTime starTime);

    //TODO: add accessRequest when implemented
    @Query("SELECT me FROM MedicalExamination me " +
            "JOIN Doctor d on (me.doctor = d) " +
            "WHERE me.doctor = ?1 " +
            "OR me.departmentType = ?2")
    List<MedicalExamination> findAllWithinDepartment(Doctor doctor, DepartmentType departmentType);

//    List<MedicalExamination> findByDoctorDoctorId(Long doctorId);
    @Query("SELECT DISTINCT me.patient FROM MedicalExamination me " +
            "JOIN Doctor d on (me.doctor = d) " +
            "WHERE me.doctor.doctorId = ?1")
    List<Patient> findDoctorsPatients(Long doctorId);

}
