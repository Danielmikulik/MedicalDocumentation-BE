package com.dm.MedicalDocumentation.patient.insuranceHistory;

import com.dm.MedicalDocumentation.request.UserLoginRequest;
import com.dm.MedicalDocumentation.response.PatientsInsuranceHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientInsuranceHistoryService {
    private final PatientsInsuranceHistoryRepository repository;
    public List<PatientsInsuranceHistoryResponse> getPatientsInsuranceHistory(String userLogin) {
        List<PatientInsuranceHistory> histories = repository.findByIdPatientUserUserLogin(userLogin);
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

}
