package com.dm.MedicalDocumentation.hospital.department.type;

import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.doctor.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentTypeService {
    private final DepartmentTypeRepository repository;
    private final DoctorRepository doctorRepository;
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

    public List<String> getDoctorsDepartmentTypes(String userLogin) {
        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("No doctor with given userLogin found!"));
        return repository.getDoctorsHistoryDepartmentTypes(doctor);
    }
}
