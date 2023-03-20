package com.dm.MedicalDocumentation.patient.insuranceHistory;

import com.dm.MedicalDocumentation.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PatientsInsuranceHistoryRepository extends JpaRepository<PatientInsuranceHistory, PatientInsuranceHistoryID> {
    List<PatientInsuranceHistory> findByIdPatientUserUserLoginOrderByDateToAsc(String userLogin);
    Optional<PatientInsuranceHistory> findFirstByIdPatientOrderByIdDateFromDesc(Patient patient);
}
