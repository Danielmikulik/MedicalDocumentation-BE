package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.accessRequest.AccessRequest;
import com.dm.MedicalDocumentation.accessRequest.AccessRequestRepository;
import com.dm.MedicalDocumentation.doctor.Doctor;
import com.dm.MedicalDocumentation.doctor.DoctorRepository;
import com.dm.MedicalDocumentation.doctor.history.DoctorHistory;
import com.dm.MedicalDocumentation.response.MedicalExamResponse;
import com.dm.MedicalDocumentation.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MedicalExaminationService {

    private final MedicalExaminationRepository repository;
    private final DoctorRepository doctorRepository;
    private final AccessRequestRepository accessRequestRepository;

    public List<MedicalExamResponse> getPatientsExams(String userLogin) {
        List<MedicalExamination> exams = repository.findByPatientUserUserLogin(userLogin);
        List<MedicalExamResponse> results = new ArrayList<>(exams.size());
        for (MedicalExamination exam : exams) {
            String disease = exam.getDisease() != null
                    ? exam.getDisease().getDiseaseType().getDiseaseTypeName()
                    : null;
            results.add(MedicalExamResponse.builder()
                    .id(exam.getMedicalExaminationId())
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

    public List<MedicalExamResponse> getDoctorsExams(String userLogin) {
        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<MedicalExamination> exams = repository.findAllWithinDepartment(doctor, doctor.getDepartment().getId().getDepartmentType());
        List<MedicalExamResponse> results = new ArrayList<>(exams.size());
        for (MedicalExamination exam : exams) {
            String disease = exam.getDisease() != null
                    ? exam.getDisease().getDiseaseType().getDiseaseTypeName()
                    : null;
            results.add(MedicalExamResponse.builder()
                    .id(exam.getMedicalExaminationId())
                    .type(exam.getType().getExaminationTypeName())
                    .disease(disease)
                    .patient(exam.getPatient().getPerson().getFullName())
                    .doctor(exam.getDoctor().getPerson().getFullName())
                    .startTime(exam.getStartTime())
                    .endTime(exam.getEndTime())
                    .build()
            );
        }
        return results;
    }

    public boolean hasUserAccess(long examId, String userLogin, Role role) {
        MedicalExamination medicalExamination = repository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("No exam with id " + examId + "found!"));
        switch (role) {
            case DOCTOR -> {
                return hasDoctorAccess(medicalExamination, userLogin);
            }
            case PATIENT -> {
                return hasPatientAccess(medicalExamination, userLogin);
            }
        }
        return false;
    }

    private boolean hasPatientAccess(MedicalExamination medicalExamination, String userLogin) {
        return medicalExamination.getPatient().getUser().getUserLogin().equals(userLogin);
    }

    private boolean hasDoctorAccess(MedicalExamination medicalExamination, String userLogin) {
        if (medicalExamination.getDoctor().getUser().getUserLogin().equals(userLogin)) {
            return true;
        }
        Doctor doctor = doctorRepository.findByUserUserLogin(userLogin)
                .orElseThrow(() -> new IllegalArgumentException("No user with given login found!"));
        for (DoctorHistory history : medicalExamination.getDoctor().getHistory()) {
            if (history.getId().getDateFrom().isBefore(medicalExamination.getStartTime().toLocalDate())
                    && (history.getDateTo() == null
                        || history.getDateTo().isAfter(medicalExamination.getStartTime().toLocalDate()))
                    && history.getDepartment().getId().getDepartmentType().equals(doctor.getDepartment().getId().getDepartmentType())) {
                return true;
            }
        }
        List<AccessRequest> requests = accessRequestRepository
                .findByMedicalExaminationMedicalExaminationId(medicalExamination.getMedicalExaminationId());
        for (AccessRequest request : requests) {
            if (request.getApproved() && request.getDoctor().getDoctorId().equals(doctor.getDoctorId())) {
                return true;
            }
        }
        return false;
    }
}
