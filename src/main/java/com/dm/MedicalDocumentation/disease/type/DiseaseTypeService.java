package com.dm.MedicalDocumentation.disease.type;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiseaseTypeService {
    private final DiseaseTypeRepository repository;
    public List<String> getDiseaseTypes() {
        List<DiseaseType> diseaseTypes = repository.findAll();
        List<String> result = new ArrayList<>(diseaseTypes.size());
        for (DiseaseType type : diseaseTypes) {
            result.add(type.getDiseaseTypeName());
        }
        return result;
    }

    public boolean recordExists(String name) {
        Optional<DiseaseType> diseaseType = repository.findByDiseaseTypeName(name);
        return diseaseType.isPresent();
    }

    public void createDiseaseType(String name) {
        DiseaseType diseaseType = DiseaseType.builder()
                .diseaseTypeName(name)
                .build();
        repository.save(diseaseType);
    }
}
