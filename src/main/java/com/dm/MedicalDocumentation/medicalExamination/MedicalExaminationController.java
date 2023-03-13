package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.response.MedicalExamResponse;
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
    private final JwtService jwtService;
    private final MedicalExaminationService service;
    @PostMapping("/patient")
    @RolesAllowed("PATIENT")
    public ResponseEntity<List<MedicalExamResponse>> getPatientsMedicalExams(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getPatientsExams(userLogin));
    }

    @PostMapping("/doctor")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<List<MedicalExamResponse>> getDoctorsMedicalExams(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getDoctorsExams(userLogin));
    }

    @GetMapping("/doctors_patients")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<List<String>> getDoctorsPatients(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getDoctorsPatients(userLogin));
    }
}
