package com.dm.MedicalDocumentation.medicalExamination.type;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
