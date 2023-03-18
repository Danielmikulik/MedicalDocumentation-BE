package com.dm.MedicalDocumentation.disease;

import com.dm.MedicalDocumentation.patient.Patient;
import com.dm.MedicalDocumentation.patient.PatientRepository;
import com.dm.MedicalDocumentation.response.DiseaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiseaseService {
    private final DiseaseRepository repository;
    private final PatientRepository patientRepository;

    public List<DiseaseResponse> getPatientsDiseasesByUserLogin(String userLogin) {
        List<Disease> diseases = repository.findByPatientUserUserLoginOrderByCuredAscDiagnosed(userLogin);
        return getDiseaseResponse(diseases);
    }

    public List<DiseaseResponse> getPatientsDiseasesByBirthNumber(String birthNumber) {
        Optional<Patient> patient = patientRepository.findByPersonBirthNumber(birthNumber);
        if (patient.isEmpty())
            return new ArrayList<>();
        List<Disease> diseases = repository.findByPatientOrderByCuredAscDiagnosed(patient.get());
        return getDiseaseResponse(diseases);
    }

    public List<DiseaseResponse> getDiseaseResponse(List<Disease> diseases) {
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
