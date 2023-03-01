package com.dm.MedicalDocumentation.disease;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    List<Disease> findByPatientUserUserLoginOrderByDiagnosed(String userLogin);
}
