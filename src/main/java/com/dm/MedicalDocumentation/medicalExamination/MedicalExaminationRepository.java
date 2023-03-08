package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicalExaminationRepository extends JpaRepository<MedicalExamination, Long> {
    List<MedicalExamination> findByPatientUserUserLogin(String userLogin);
    List<MedicalExamination> findByDoctorUserUserLogin(String userLogin);

    //TODO: add accessRequest when implemented
    @Query("SELECT me FROM MedicalExamination me " +
            "JOIN Doctor d on (me.doctor = d) " +
            "WHERE me.doctor = ?1 " +
            "OR me.departmentType = ?2")
    List<MedicalExamination> findAllWithinDepartment(Doctor doctor, DepartmentType departmentType);

}
