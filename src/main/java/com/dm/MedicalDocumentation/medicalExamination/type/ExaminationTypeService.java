package com.dm.MedicalDocumentation.medicalExamination.type;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExaminationTypeService {
    private final ExaminationTypeRepository repository;
    public List<String> getExaminationTypes() {
        List<ExaminationType> examinationTypes = repository.findAll();
        List<String> result = new ArrayList<>(examinationTypes.size());
        for (ExaminationType type : examinationTypes) {
            result.add(type.getExaminationTypeName());
        }
        return result;
    }

    public boolean recordExists(String name) {
        Optional<ExaminationType> examinationType = repository.findByExaminationTypeName(name);
        return examinationType.isPresent();
    }

    public void createExaminationType(String name) {
        ExaminationType examinationType = ExaminationType.builder()
                .examinationTypeName(name)
                .build();
        repository.save(examinationType);
    }
}
