package com.dm.MedicalDocumentation.prescription;

import com.dm.MedicalDocumentation.medicalExamination.MedicalExaminationService;
import com.dm.MedicalDocumentation.request.UserLoginRequest;
import com.dm.MedicalDocumentation.response.MedicalExamForPatient;
import com.dm.MedicalDocumentation.response.PrescriptionResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prescription")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PrescriptionController {
    private final PrescriptionService service;
    @PostMapping("/patient_prescriptions")
    @RolesAllowed("PATIENT")
    public ResponseEntity<List<PrescriptionResponse>> getPatientInfo(
            @RequestBody UserLoginRequest request
    ) {
        return ResponseEntity.ok(service.getPatientsPrescriptions(request));
    }
}
