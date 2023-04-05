package com.dm.MedicalDocumentation.hospital;

import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HospitalRepository extends JpaRepository<Hospital, Integer> {
    Optional<Hospital> findByHospitalName(String hospitalName);

    @Query("SELECT COUNT(h) " +
            "FROM Hospital h " +
            "JOIN Department d ON (d.id.hospital = h) " +
            "WHERE d.id.departmentType = ?1")
    long getCountByDepartmentType(DepartmentType departmentType);
}
