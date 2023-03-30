package com.dm.MedicalDocumentation.hospital.department.type;

import com.dm.MedicalDocumentation.doctor.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DepartmentTypeRepository extends JpaRepository<DepartmentType, Integer> {
    Optional<DepartmentType> findByDepartmentTypeName(String departmentTypeName);

    @Query("SELECT DISTINCT dh.department.id.departmentType.departmentTypeName " +
            "FROM DoctorHistory dh " +
            "WHERE dh.id.doctor = ?1")
    List<String> getDoctorsHistoryDepartmentTypes(Doctor doctor);
}
