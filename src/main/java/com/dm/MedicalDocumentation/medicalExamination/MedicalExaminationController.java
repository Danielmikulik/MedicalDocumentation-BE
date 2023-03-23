package com.dm.MedicalDocumentation.medicalExamination;

import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.request.MedicalExamRequest;
import com.dm.MedicalDocumentation.request.StringRequest;
import com.dm.MedicalDocumentation.response.CustomPage;
import com.dm.MedicalDocumentation.response.MedicalExamResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/doctor")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<CustomPage<MedicalExamResponse>> getDoctorsMedicalExams(
            @RequestHeader (name="Authorization") String token,
            @RequestParam int pageIndex,
            @RequestParam int pageSize,
            @RequestBody StringRequest birthNumber
            ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        String department = (String) jwtService.extractClaim(token.substring(7), "department");
        boolean isGeneralPractitioner = department.equalsIgnoreCase("Ambulancia všeobecného lekára");
        Pageable page = PageRequest.of(pageIndex, pageSize);
        return ResponseEntity.ok(service.getDoctorsExams(userLogin, birthNumber.getValue(), isGeneralPractitioner, page));
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RolesAllowed("DOCTOR")
    public ResponseEntity<String> confirmAccessRequests(
            @RequestHeader(name="Authorization") String token,
            @ModelAttribute MedicalExamRequest request
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.createMedicalExam(userLogin, request));
    }
}
