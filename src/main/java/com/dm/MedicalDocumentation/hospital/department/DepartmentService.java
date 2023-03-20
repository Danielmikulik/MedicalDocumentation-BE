package com.dm.MedicalDocumentation.hospital.department;

import com.dm.MedicalDocumentation.hospital.Hospital;
import com.dm.MedicalDocumentation.hospital.HospitalRepository;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository repository;
    private final HospitalRepository hospitalRepository;
    private final DepartmentTypeRepository departmentTypeRepository;

    public boolean recordExists(DepartmentRequest request) {
        Optional<Department> department = repository.findByIdHospitalHospitalNameAndIdDepartmentTypeDepartmentTypeName(
                request.getHospital(), request.getDepartmentType()
        );
        return department.isPresent();
    }

    public void createDepartment(DepartmentRequest request) {
        Hospital hospital = hospitalRepository.findByHospitalName(request.getHospital())
                .orElseThrow(() -> new IllegalArgumentException("Hospital " + request.getHospital() + "does not exist."));
        DepartmentType departmentType = departmentTypeRepository.findByDepartmentTypeName(request.getDepartmentType())
                .orElseThrow(() -> new IllegalArgumentException("Department type " + request.getDepartmentType() + "does not exist."));
        Department department = Department.builder()
                .id(DepartmentID.builder()
                        .hospital(hospital)
                        .departmentType(departmentType)
                        .build())
                .build();
        repository.save(department);
    }
}
