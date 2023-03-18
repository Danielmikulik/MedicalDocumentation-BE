package com.dm.MedicalDocumentation.disease;

import com.dm.MedicalDocumentation.disease.type.DiseaseType;
import com.dm.MedicalDocumentation.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiseaseRepository extends JpaRepository<Disease, Long> {
    List<Disease> findByPatientUserUserLoginOrderByCuredAscDiagnosed(String userLogin);
    List<Disease> findByPatientOrderByCuredAscDiagnosed(Patient patient);
    Optional<Disease> findByPatientAndDiseaseTypeAndCured(Patient patient, DiseaseType diseaseType, LocalDateTime cured);
}
