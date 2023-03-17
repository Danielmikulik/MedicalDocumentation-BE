package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.request.MedicalExamRequest;
import com.dm.MedicalDocumentation.response.CustomPage;
import com.dm.MedicalDocumentation.response.MedicalExamResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @GetMapping("/patient")
    @RolesAllowed("PATIENT")
    public ResponseEntity<CustomPage<MedicalExamResponse>> getPatientsMedicalExams(
            @RequestHeader (name="Authorization") String token,
            @RequestParam int pageIndex,
            @RequestParam int pageSize
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        Pageable page = PageRequest.of(pageIndex, pageSize);
        return ResponseEntity.ok(service.getPatientsExams(userLogin, page));
    }

    @GetMapping("/doctor")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<CustomPage<MedicalExamResponse>> getDoctorsMedicalExams(
            @RequestHeader (name="Authorization") String token,
            @RequestParam int pageIndex,
            @RequestParam int pageSize,
            @RequestParam(required = false) String birthNumber
            ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        String department = (String) jwtService.extractClaim(token.substring(7), "department");
        boolean isGeneralPractitioner = department.equalsIgnoreCase("Ambulancia všeobecného lekára");
        Pageable page = PageRequest.of(pageIndex, pageSize);
        return ResponseEntity.ok(service.getDoctorsExams(userLogin, birthNumber, isGeneralPractitioner, page));
    }

    @GetMapping("/doctors_patients")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<List<String>> getDoctorsPatients(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        String department = (String) jwtService.extractClaim(token.substring(7), "department");
        boolean isGeneralPractitioner = department.equalsIgnoreCase("Ambulancia všeobecného lekára");
        return ResponseEntity.ok(service.getDoctorsPatients(userLogin, isGeneralPractitioner));
    }

    @PostMapping("/create")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<String> confirmAccessRequests(
            @RequestHeader(name="Authorization") String token,
            @RequestBody MedicalExamRequest request
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.createMedicalExam(userLogin, request));
    }
}
