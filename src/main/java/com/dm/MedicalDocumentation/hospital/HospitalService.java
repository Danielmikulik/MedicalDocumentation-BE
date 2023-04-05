package com.dm.MedicalDocumentation.hospital;

import com.dm.MedicalDocumentation.hospital.department.type.DepartmentType;
import com.dm.MedicalDocumentation.hospital.department.type.DepartmentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HospitalService {
    private final HospitalRepository repository;
    private final DepartmentTypeRepository departmentTypeRepository;

    public boolean recordExists(String name) {
        Optional<Hospital> hospital = repository.findByHospitalName(name);
        return hospital.isPresent();
    }

    public void createHospital(String name) {
        Hospital hospital = Hospital.builder()
                .hospitalName(name)
                .build();
        repository.save(hospital);
    }

    public List<String> getHospitals() {
        List<Hospital> hospitals = repository.findAll();
        List<String> result = new ArrayList<>(hospitals.size());
        for (Hospital hospital : hospitals) {
            result.add(hospital.getHospitalName());
        }
        return result;
    }

    public Long getHospitalCount(String departmentTypeName) {
        if (!departmentTypeName.isBlank()) {
            DepartmentType departmentType = departmentTypeRepository.findByDepartmentTypeName(departmentTypeName)
                    .orElseThrow(() -> new IllegalArgumentException("No department with given name found!"));
            return repository.getCountByDepartmentType(departmentType);
        }
        return repository.count();
    }
}
