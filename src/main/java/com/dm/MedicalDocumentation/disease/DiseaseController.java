package com.dm.MedicalDocumentation.disease;

import com.dm.MedicalDocumentation.config.JwtService;
import com.dm.MedicalDocumentation.request.StringRequest;
import com.dm.MedicalDocumentation.response.DiseaseResponse;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/disease")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class DiseaseController {
    private final JwtService jwtService;
    private final DiseaseService service;

    @PostMapping
    @RolesAllowed({"PATIENT", "DOCTOR"})
    public ResponseEntity<List<DiseaseResponse>> getPatientsDiseases(
            @RequestHeader (name="Authorization") String token,
            @RequestBody StringRequest birthNumber
    ) {
        String userLogin = jwtService.extractUsername(token.substring(7));
        List<DiseaseResponse> response = birthNumber.getValue() == null
                ? service.getPatientsDiseasesByUserLogin(userLogin)
                : service.getPatientsDiseasesByBirthNumber(birthNumber.getValue());
        return ResponseEntity.ok(response);
    }
}
