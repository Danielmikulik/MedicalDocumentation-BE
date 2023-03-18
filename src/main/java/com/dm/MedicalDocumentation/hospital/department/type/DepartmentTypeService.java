package com.dm.MedicalDocumentation.hospital.department.type;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentTypeService {
    private final DepartmentTypeRepository repository;
    public List<String> getDepartments() {
        List<DepartmentType> departmentTypes = repository.findAll();
        List<String> result = new ArrayList<>(departmentTypes.size());
        for (DepartmentType type : departmentTypes) {
            result.add(type.getDepartmentTypeName());
        }
        return result;
    }

    public void createDepartmentType(String name) {
        DepartmentType departmentType = DepartmentType.builder()
                .departmentTypeName(name)
                .build();
        repository.save(departmentType);
    }

    public boolean recordExists(String name) {
        Optional<DepartmentType> departmentType = repository.findByDepartmentTypeName(name);
        return departmentType.isPresent();
    }
}
