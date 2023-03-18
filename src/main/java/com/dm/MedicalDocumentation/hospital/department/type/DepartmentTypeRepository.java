package com.dm.MedicalDocumentation.hospital.department.type;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentTypeRepository extends JpaRepository<DepartmentType, Integer> {
    Optional<DepartmentType> findByDepartmentTypeName(String departmentTypeName);
}
