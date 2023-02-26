package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.request.UserLoginRequest;
import com.dm.MedicalDocumentation.response.MedicalExamForPatient;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/med_exams")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class MedicalExaminationController {

    private final MedicalExaminationService service;
    @PostMapping("/patient_exams")
    @RolesAllowed("PATIENT")
    public ResponseEntity<List<MedicalExamForPatient>> getPatientInfo(
            @RequestBody UserLoginRequest request
    ) {
        return ResponseEntity.ok(service.gePatientsExams(request));
    }
}
