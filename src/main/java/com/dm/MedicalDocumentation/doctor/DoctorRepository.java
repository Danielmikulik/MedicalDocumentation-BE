package com.dm.MedicalDocumentation.doctor;

import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserUserLogin(String userLogin);
    Optional<Doctor> findByPersonBirthNumber(String birthNumber);
    List<Doctor> findByDepartmentIdDepartmentType(DepartmentType departmentType);
}
