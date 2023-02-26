package com.dm.MedicalDocumentation.medicalExamination;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalExaminationRepository extends JpaRepository<MedicalExamination, Integer> {
    List<MedicalExamination> findByPatientUserUserLogin(String userLogin);

}
