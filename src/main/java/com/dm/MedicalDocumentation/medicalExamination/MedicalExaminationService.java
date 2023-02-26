package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.request.UserLoginRequest;
import com.dm.MedicalDocumentation.response.MedicalExamForPatient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalExaminationService {

    private final MedicalExaminationRepository repository;

    public List<MedicalExamForPatient> gePatientsExams(UserLoginRequest request) {
        List<MedicalExamination> exams = repository.findByPatientUserUserLogin(request.getUserLogin());
        List<MedicalExamForPatient> results = new ArrayList<>(exams.size());
        for (MedicalExamination exam : exams) {
            String disease = exam.getDisease() != null
                    ? exam.getDisease().getDiseaseType().getDiseaseTypeName()
                    : null;
            results.add(MedicalExamForPatient.builder()
                    .type(exam.getType().getExaminationTypeName())
                    .disease(disease)
                    .doctor(exam.getDoctor().getPerson().getFullName())
                    .startTime(exam.getStartTime())
                    .endTime(exam.getEndTime())
                    .build()
            );
        }
        return results;
    }
}
