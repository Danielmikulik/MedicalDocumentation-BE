package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MedicalExaminationRepository extends JpaRepository<MedicalExamination, Long> {
    List<MedicalExamination> findByPatientUserUserLogin(String userLogin);
    List<MedicalExamination> findByDoctorUserUserLogin(String userLogin);

    @Query("SELECT me FROM MedicalExamination me " +
            "JOIN Doctor d on (me.doctor = d) " +
            "JOIN DoctorHistory dh on (d = dh.id.doctor) " +
            "WHERE me.doctor = ?1 " +
            "OR d.department.id.departmentType = ?2 " +
            "OR dh.department.id.departmentType = ?2 AND dh.id.dateFrom <= me.startTime AND dh.dateTo > me.startTime")
    List<MedicalExamination> findAllWithinDepartment(Doctor doctor, DepartmentType departmentType);

}
