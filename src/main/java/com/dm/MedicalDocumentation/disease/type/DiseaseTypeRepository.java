package com.dm.MedicalDocumentation.disease.type;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiseaseTypeRepository extends JpaRepository<DiseaseType, Integer> {
    Optional<DiseaseType> findByDiseaseTypeName(String diseaseTypeName);
}
