package com.dm.MedicalDocumentation.patient.insuranceHistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientsInsuranceHistoryRepository extends JpaRepository<PatientInsuranceHistory, PatientInsuranceHistoryID> {

    List<PatientInsuranceHistory> findByIdPatientUserUserLogin(String userLogin);

}
