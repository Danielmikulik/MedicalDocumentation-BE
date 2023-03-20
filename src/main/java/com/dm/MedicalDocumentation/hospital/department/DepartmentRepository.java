package com.dm.MedicalDocumentation.hospital.department;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, DepartmentID> {
    Optional<Department> findByIdHospitalHospitalNameAndIdDepartmentTypeDepartmentTypeName(String hospitalName, String departmentTypeName);
}
