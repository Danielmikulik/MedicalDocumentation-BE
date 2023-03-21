package com.dm.MedicalDocumentation.patient.insuranceHistory;

import com.dm.MedicalDocumentation.patient.Patient;
import com.dm.MedicalDocumentation.response.PatientsInsuranceHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PatientInsuranceHistoryService {
    private final PatientsInsuranceHistoryRepository repository;
    public List<PatientsInsuranceHistoryResponse> getPatientsInsuranceHistory(String userLogin) {
        List<PatientInsuranceHistory> histories = repository.findByIdPatientUserUserLoginOrderByDateToAsc(userLogin);
        List<PatientsInsuranceHistoryResponse> results = new ArrayList<>(histories.size());
        for (PatientInsuranceHistory history : histories) {
            results.add(PatientsInsuranceHistoryResponse.builder()
                    .insurance(history.getInsurance().getInsuranceName())
                    .since(history.getId().getDateFrom())
                    .till(history.getDateTo())
                    .build());
        }
        return results;
    }

    public void createHealthInsuranceHistory(Patient patient, boolean newPatient) {
        LocalDate dateFrom;
        if (!newPatient) {
            Optional<PatientInsuranceHistory> latestHistory = repository.findFirstByIdPatientOrderByIdDateFromDesc(patient);
            if (latestHistory.isPresent()) {
                latestHistory.get().setDateTo(LocalDate.now());
                repository.save(latestHistory.get());
            }
            dateFrom = LocalDate.now();
        } else {
            dateFrom = patient.getPerson().getBirthDate();
        }

        PatientInsuranceHistory history = PatientInsuranceHistory.builder()
                .id(PatientInsuranceHistoryID.builder()
                        .patient(patient)
                        .dateFrom(dateFrom)
                        .build())
                .dateTo(null)
                .insurance(patient.getHealthInsurance())
                .build();

        repository.save(history);
    }
}
