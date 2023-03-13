package com.dm.MedicalDocumentation.hospital.department.type;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
