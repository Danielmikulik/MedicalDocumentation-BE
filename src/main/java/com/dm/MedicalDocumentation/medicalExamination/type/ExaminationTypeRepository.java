package com.dm.MedicalDocumentation.medicalExamination.type;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExaminationTypeRepository extends JpaRepository<ExaminationType, Integer> {
    Optional<ExaminationType> findByExaminationTypeName(String examinationTypeName);
}
