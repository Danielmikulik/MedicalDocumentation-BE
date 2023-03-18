package com.dm.MedicalDocumentation.patient;

import com.dm.MedicalDocumentation.request.StringRequest;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PatientController {
    private final PatientService service;

    @PostMapping("/name")
    @RolesAllowed("DOCTOR")
    public ResponseEntity<String> getPatientByBirthNumber(
            @RequestBody StringRequest request
    ) {
        return ResponseEntity.ok(service.getPatientNameByBirthNumber(request.getValue()));
    }
}
