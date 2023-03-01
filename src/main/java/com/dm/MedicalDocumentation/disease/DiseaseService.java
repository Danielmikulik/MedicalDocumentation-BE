package com.dm.MedicalDocumentation.disease;

import com.dm.MedicalDocumentation.request.UserLoginRequest;
import com.dm.MedicalDocumentation.response.DiseaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiseaseService {
    private final DiseaseRepository repository;
    public List<DiseaseResponse> getPatientsDiseases(String userLogin) {
        List<Disease> diseases = repository.findByPatientUserUserLoginOrderByDiagnosed(userLogin);
        List<DiseaseResponse> results = new ArrayList<>(diseases.size());
        for (Disease disease : diseases) {
            results.add(DiseaseResponse.builder()
                    .disease(disease.getDiseaseType().getDiseaseTypeName())
                    .diagnosed(disease.getDiagnosed())
                    .cured(disease.getCured())
                    .build());
        }
        return results;
    }
}
