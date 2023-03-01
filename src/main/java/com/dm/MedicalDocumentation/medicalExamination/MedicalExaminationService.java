package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.request.UserLoginRequest;
import com.dm.MedicalDocumentation.response.MedicalExamDoctor;
import com.dm.MedicalDocumentation.response.MedicalExamPatient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalExaminationService {

    private final MedicalExaminationRepository repository;

    public List<MedicalExamPatient> getPatientsExams(String userLogin) {
        List<MedicalExamination> exams = repository.findByPatientUserUserLogin(userLogin);
        List<MedicalExamPatient> results = new ArrayList<>(exams.size());
        for (MedicalExamination exam : exams) {
            String disease = exam.getDisease() != null
                    ? exam.getDisease().getDiseaseType().getDiseaseTypeName()
                    : null;
            results.add(MedicalExamPatient.builder()
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

    public List<MedicalExamDoctor> getDoctorsExams(String userLogin) {
        List<MedicalExamination> exams = repository.findByDoctorUserUserLogin(userLogin);
        List<MedicalExamDoctor> results = new ArrayList<>(exams.size());
        for (MedicalExamination exam : exams) {
            String disease = exam.getDisease() != null
                    ? exam.getDisease().getDiseaseType().getDiseaseTypeName()
                    : null;
            results.add(MedicalExamDoctor.builder()
                    .type(exam.getType().getExaminationTypeName())
                    .disease(disease)
                    .patient(exam.getPatient().getPerson().getFullName())
                    .startTime(exam.getStartTime())
                    .endTime(exam.getEndTime())
                    .build()
            );
        }
        return results;
    }
}
