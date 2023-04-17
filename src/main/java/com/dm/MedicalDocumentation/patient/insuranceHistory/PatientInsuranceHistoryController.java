package com.dm.MedicalDocumentation.patient.insuranceHistory;

import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.response.PatientsInsuranceHistoryResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient_insurance_history")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://localhost:3000"})
public class PatientInsuranceHistoryController {
    private final JwtService jwtService;
    private final PatientInsuranceHistoryService service;

    @PostMapping
    @RolesAllowed("PATIENT")
    public ResponseEntity<List<PatientsInsuranceHistoryResponse>> getPatientsInsuranceHistory(
            @RequestHeader (name="Authorization") String token
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        return ResponseEntity.ok(service.getPatientsInsuranceHistory(userLogin));
    }
}
