package com.dm.MedicalDocumentation.accessRequest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> {
    List<AccessRequest> findByPatientUserUserLogin(String userLogin);
    List<AccessRequest> findByMedicalExaminationMedicalExaminationId(Long medicalExamId);
}
