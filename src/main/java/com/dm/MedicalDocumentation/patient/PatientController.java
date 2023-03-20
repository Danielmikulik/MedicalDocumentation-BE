package com.dm.MedicalDocumentation.patient;

import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.request.StringRequest;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PatientController {
    private final JwtService jwtService;
    private final PatientService service;

    @PostMapping("/name")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<String> getPatientByBirthNumber(
            @RequestBody StringRequest request
    ) {
        return ResponseEntity.ok(service.getPatientNameByBirthNumber(request.getValue()));
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
}
